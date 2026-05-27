package dev.upcraft.gradle.multiloader

import gradle.kotlin.dsl.accessors._3c984467cfe6063166439ec0710b6c00.sourceSets
import gradle.kotlin.dsl.accessors._3c984467cfe6063166439ec0710b6c00.versionCatalogs
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.DependencyScopeConfiguration
import org.gradle.api.artifacts.ModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.attributes.Attribute
import org.gradle.api.attributes.AttributeContainer
import org.gradle.api.attributes.HasAttributes
import org.gradle.api.attributes.HasConfigurableAttributes
import org.gradle.kotlin.dsl.getByType
import org.gradle.language.jvm.tasks.ProcessResources
import kotlin.collections.mapOf
import kotlin.jvm.optionals.getOrNull

val loaderAttribute = Attribute.of("io.github.mcgradleconventions.loader", String::class.java)

/**
 * [MC Gradle Conventions](https://github.com/mcgradleconventions)
 */
public fun Project.applyMcGradleConventions(loader: String, configurations: Collection<String>? = null) {
    if(configurations == null) {
        pluginManager.withPlugin("net.fabricmc.fabric-loom") {
            listOf("includeInternal", "modCompileClasspath").forEach {
                project.configurations.named(it).configure {
                    attributes {
                        loaderAttribute(loader)
                    }
                }
            }
        }
    }

    afterEvaluate {
        val cfgs = buildList {
            if(configurations != null) {
                addAll(configurations)
            }
            else {
                add("apiElements")
                add("runtimeElements")
                add("sourcesElements")

                if(javadocEnabled) {
                    add("javadocElements")
                }
            }

            sourceSets.forEach {
                add(it.compileClasspathConfigurationName)
                add(it.runtimeClasspathConfigurationName)
            }
        }
        cfgs.forEach {
            this.configurations.named(it).configure {
                attributes {
                    loaderAttribute(loader)
                }
            }
        }
    }
}

public fun AttributeContainer.loaderAttribute(loader: String) {
    attribute(loaderAttribute, loader)
}

public fun ProcessResources.configureModProperties() {
    val libs = project.versionCatalogs.named("libs")
    val expandProps = mapOf(
        "version" to project.version,
        "maven_group_id" to project.group,
        "mod_id" to project.property("mod_id"),
        "mod_display_name" to project.providers.gradleProperty("mod_display_name").get(),
        "mod_description" to project.providers.gradleProperty("mod_description").get(),
        "sources_url" to project.providers.gradleProperty("sources_url").get(),
        "issues_url" to project.providers.gradleProperty("issues_url").get(),
        "license_url" to project.providers.gradleProperty("license_url").get(),
        "discord_url" to project.providers.gradleProperty("discord_url").get(),
        "homepage_url" to project.providers.gradleProperty("homepage_url").get(),
        "curseforge_id" to project.providers.gradleProperty("curseforge_id").get(),
        "modrinth_id" to project.providers.gradleProperty("modrinth_id").get(),

        "java_version" to libs.findVersion("java").orElseThrow(),
        "minecraft_version" to libs.findVersion("minecraft").orElseThrow(),
        "neoforge_version" to libs.findVersion("neoforge").getOrNull(),
        "fabric_loader_version" to libs.findVersion("fabric_loader").getOrNull(),
    )

    filesMatching("META-INF/*mods.toml") {
        expand(expandProps)
    }

    filesMatching(listOf("pack.mcmeta", "*.mod.json", "*.mixins.json")) {
        expand(expandProps.mapValues { it.value.toString().replace("\n", "\\n") })
    }

    inputs.properties(expandProps)
}

internal val Project.javadocEnabled get() = providers.gradleProperty("dev.upcraft.gradle.multiloader.enableJavadoc").orNull.toBoolean()
