import org.gradle.api.problems.internal.GradleCoreProblemGroup.versionCatalog
import org.gradle.kotlin.dsl.idea
import org.gradle.kotlin.dsl.`kotlin-dsl`
import org.gradle.kotlin.dsl.`maven-publish`

plugins {
    idea
    `kotlin-dsl`
    `maven-publish`
    `version-catalog`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    compileOnly(gradleApi())
    compileOnly(gradleKotlinDsl())
}

catalog {
    versionCatalog {
        from(files("../gradle/libs.versions.toml"))
    }
}
