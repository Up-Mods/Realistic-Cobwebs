@file:OptIn(ExperimentalTime::class)

import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.time.toJavaInstant

plugins {
    idea
    `maven-publish`
    alias(libs.plugins.idea.ext)
    alias(libs.plugins.moddevgradle) apply false
    alias(libs.plugins.fabric.loom) apply false
}

println(DateTimeFormatter::class.qualifiedName)

val now = Instant.fromEpochSeconds(Clock.System.now().epochSeconds).toJavaInstant()
val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yy.M.d").withZone(ZoneId.of("UTC"))
val buildTime = providers.environmentVariable("BUILD_TIME").orElse(provider { formatter.format(now) })

val tag = providers.environmentVariable("TAG")
val isPreviewBuild = tag.orNull?.matches(Regex(".+-.+")) ?: true
val buildNumber = providers.environmentVariable("BUILD_NUMBER").map { "build.${it}" }.orElse(buildTime)

version = tag.orElse(provider { buildString {
    append("development")
    append("-")
    append(libs.versions.minecraft.get())
    if(isPreviewBuild) {
        append("+")
        append(buildNumber.get())
    }
} }).get()

println("Building ${project.name} $version")

// IDEA no longer automatically downloads sources/javadoc jars for dependencies, so we need to explicitly enable the behavior.
idea {
	module {
		isDownloadSources = true
		isDownloadJavadoc = true
	}
}
