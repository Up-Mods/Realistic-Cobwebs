@file:OptIn(ExperimentalTime::class)

package dev.upcraft.gradle.multiloader

import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

plugins {
    `java-library`
    `maven-publish`
}

version = rootProject.version

val libs = versionCatalogs.named("libs")

val minecraftVersion: String = libs.findVersion("minecraft").orElseThrow().toString()
println("Minecraft: $minecraftVersion")

val javaVersion = libs.findVersion("java").orElseThrow().toString().toInt()
println("Java: $javaVersion")

val now = Instant.fromEpochSeconds(Clock.System.now().epochSeconds)

// FIXME workaround for appdirs transitively requiring newer JNA but we are locked due to MC
libs.findLibrary("jna").ifPresent {
    configurations.configureEach {
        resolutionStrategy.force(it)
    }
}

repositories {
    mavenCentral()

    // mod platforms
    exclusiveContent {
        forRepository {
            maven(uri("https://api.modrinth.com/maven")) {
                name = "Modrinth"
            }
        }
        filter {
            includeGroup("maven.modrinth")
        }
    }
    exclusiveContent {
        forRepository {
            maven(uri("https://www.cursemaven.com")) {
                name = "CurseMaven"
            }
        }
        filter {
            includeGroup("curse.maven")
        }
    }

    maven(uri("https://maven.fabricmc.net")) {
        name = "FabricMC"
    }

    maven(uri("https://maven.neoforged.net/releases")) {
        name = "NeoForge"
    }

    maven(uri("https://maven.uuid.gg/releases"))

    // third party maven repositories
    // TODO set up pass-through relay for these
    maven(uri("https://maven.ladysnake.org/releases")) {
        name = "Ladysnake"
    }

    maven(uri("https://maven.teamresourceful.com/repository/maven-releases")) {
        name = "TeamResourceful"
    }

    maven(uri("https://maven.terraformersmc.com/releases")) {
        name = "TerraformersMC"
    }
}

dependencies {
    testImplementation(libs.findLibrary("junit_api").orElseThrow())
    testRuntimeOnly(libs.findLibrary("junit_launcher").orElseThrow())
    testRuntimeOnly(libs.findLibrary("junit_engine").orElseThrow())
}

base {
    archivesName = "${rootProject.name}-${project.name}-${minecraftVersion}"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(javaVersion)
        vendor = JvmVendorSpec.MICROSOFT
    }

    withSourcesJar()
    if (project.javadocEnabled) {
        withJavadocJar()
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(javaVersion)
    options.compilerArgs.add("-Xlint:unchecked,deprecation")
}

tasks.named<Jar>("jar").configure {
    from(rootProject.file("LICENSE.md")) {
        rename("LICENSE.md", "LICENSE_${rootProject.name}.md")
    }

    manifest.attributes(
        mapOf<String, Any>(
            "Specification-Title" to rootProject.name,
            "Specification-Vendor" to "Up",
            "Specification-Version" to archiveVersion,

            "Implementation-Title" to project.name,
            "Implementation-Vendor" to "Up",
            "Implementation-Version" to archiveVersion,
            "Implementation-Timestamp" to now.toString(),
            "Timestamp" to now.toEpochMilliseconds(),

            "Built-On-Java" to "${providers.systemProperty("java.vm.version").orNull} (${providers.systemProperty("java.vm.vendor").orNull})",
            "Built-On-Minecraft" to minecraftVersion
        )
    )
}

tasks.named<Jar>("sourcesJar").configure {
    from(rootProject.file("LICENSE.md")) {
        rename("LICENSE.md", "LICENSE_${rootProject.name}.md")
    }
}

tasks.withType<Javadoc>().configureEach {
    (options as StandardJavadocDocletOptions).tags(listOf("reason", "implNote"))
}

tasks.named<Test>("test").configure {
    useJUnitPlatform()
}

tasks.withType<ProcessResources>().configureEach {
    filteringCharset = "UTF-8"
}

tasks.named<ProcessResources>("processResources").configure {
    configureModProperties()
}

publishing {
    publications {
        register("mavenJava", MavenPublication::class) {
            from(components["java"])
        }
    }

    providers.environmentVariable("MAVEN_UPLOAD_URL").orNull?.let { url ->
        repositories {
            maven(uri(url)) {
                credentials {
                    username = providers.environmentVariable("MAVEN_UPLOAD_USERNAME").orNull
                    password = providers.environmentVariable("MAVEN_UPLOAD_PASSWORD").orNull
                }
            }
        }
    }
}

// Declare capabilities on the outgoing configurations.
// Read more about capabilities here: https://docs.gradle.org/current/userguide/component_capabilities.html#sec:declaring-additional-capabilities-for-a-local-component
buildList {
    add("apiElements")
    add("runtimeElements")
    add("sourcesElements")

    if (project.javadocEnabled) {
        add("javadocElements")
    }
}.forEach { variant ->
    configurations.named(variant).configure {
        outgoing {
            capability("$group:${project.name}:$version")
            capability("$group:${rootProject.name}:$version")
        }
    }

    publishing {
        publications.withType<MavenPublication>().configureEach {
            suppressPomMetadataWarningsFor(variant)
        }
    }
}
