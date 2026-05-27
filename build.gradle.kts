@file:OptIn(ExperimentalTime::class)

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

val now = Instant.fromEpochSeconds(Clock.System.now().epochSeconds).toJavaInstant()
val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yy.Md.Hm")
val buildTime = providers.environmentVariable("BUILD_TIME").orElse(provider { formatter.format(now) })

val tag = providers.environmentVariable("TAG")
val isPreviewBuild = tag.orNull?.matches(Regex(".+-.+")) ?: false
val buildNumber = tag.orElse(providers.environmentVariable("BUILD_NUMBER").map { "build.${it}" }.orElse(buildTime))

version = tag.orElse(provider { buildString {
    append("${libs.versions.minecraft.get()}-development")
    if(isPreviewBuild && !tag.isPresent) {
        append(buildNumber.map { "+${it}" }.getOrElse(""))
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
