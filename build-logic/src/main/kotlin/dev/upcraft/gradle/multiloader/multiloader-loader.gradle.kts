package dev.upcraft.gradle.multiloader

plugins {
    id("dev.upcraft.gradle.multiloader.multiloader-common")
}

val commonJavaDep by configurations.dependencyScope("commonJavaDep")
val commonJava by configurations.resolvable("commonJava") { extendsFrom(commonJavaDep) }

val commonResourcesDep by configurations.dependencyScope("commonResourcesDep")
val commonResources by configurations.resolvable("commonResources") { extendsFrom(commonResourcesDep) }

dependencies {
    compileOnly(project(":${rootProject.name}-Common")) {
        attributes { loaderAttribute("common") }
    }

    commonJavaDep(project(":${rootProject.name}-Common", "commonJava"))
    commonResourcesDep(project(":${rootProject.name}-Common", "commonResources"))
}

tasks.named<JavaCompile>("compileJava").configure {
    dependsOn(commonJava)
    source(commonJava)
}

tasks.named<ProcessResources>("processResources").configure {
    dependsOn(commonResources)
    from(commonResources)
}

tasks.named<Javadoc>("javadoc").configure {
    dependsOn(commonJava)
    source(commonJava)
}

tasks.named<Jar>("sourcesJar").configure {
    dependsOn(commonJava, commonResources)
    from(commonJava, commonResources)
}
