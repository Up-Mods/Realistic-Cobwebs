pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven {
            name = "NeoForge"
            url = uri("https://maven.neoforged.net/releases")
        }
        maven {
            name = "Fabric"
            url = uri("https://maven.fabricmc.net")
        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "Realistic-Cobwebs"

includeBuild("build-logic")

listOf("Common", "Fabric", "NeoForge").forEach {
    include(it)
    project(":$it").name = "${rootProject.name}-$it"
}

val env: Map<String, String?> = System.getenv()

buildCache {
    remote<HttpBuildCache> {
        url = uri("https://ci-cache.uuid.gg/cache")
        if (env["CI"] == "true" && env["GRADLE_BUILD_CACHE_USER"] != null && env["GRADLE_BUILD_CACHE_TOKEN"] != null) {
            isPush = true
            credentials {
                username = env["GRADLE_BUILD_CACHE_USER"]
                password = env["GRADLE_BUILD_CACHE_TOKEN"]
            }
        }
    }
}
