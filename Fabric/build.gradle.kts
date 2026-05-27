@file:Suppress("UnstableApiUsage")

import dev.upcraft.gradle.multiloader.applyMcGradleConventions
import net.fabricmc.loom.task.LoomTasks
import org.jetbrains.gradle.ext.runConfigurations
import org.jetbrains.gradle.ext.settings


plugins {
    id("dev.upcraft.gradle.multiloader.multiloader-loader")
    id("org.jetbrains.gradle.plugin.idea-ext")
    id("net.fabricmc.fabric-loom")
}
applyMcGradleConventions("fabric")

val modId: String = providers.gradleProperty("mod_id").get()

dependencies {
    minecraft(libs.minecraft)

    compileOnly(libs.autoservice.annotations)
    annotationProcessor(libs.autoservice)

    implementation(libs.fabric.loader)
    implementation(libs.fabric.api)

    implementation(libs.resourcefulconfig.fabric) {
      isTransitive = false
	}

//    compileOnly(variantOf(libs.emi.fabric) { classifier("api") }) {
//        isTransitive = false
//    }
//    localRuntime(libs.emi.fabric) {
//        isTransitive = false
//    }

    compileOnly(libs.modmenu.fabric) {
        isTransitive = false
    }
    localRuntime(libs.modmenu.fabric) {
        isTransitive = false
    }

    testImplementation(libs.junit.api)
    testRuntimeOnly(libs.junit.launcher)
    testRuntimeOnly(libs.junit.engine)
}

loom {
    mods {
        create(modId) {
            // Tell Loom about each source set used by your mod here. This ensures that your mod's classes are properly transformed by Loader.
            sourceSet(sourceSets["main"])
            sourceSet(project(":Realistic-Cobwebs-Common").sourceSets["main"])
        }
    }

    accessWidenerPath = file("src/main/resources/${modId}.classtweaker")

    runs {
        named("client") {
            client()
            configName = "Fabric Client"
            runDir("run")

            if (project.hasProperty("mc_uuid")) {
                programArg("--uuid=${project.findProperty("mc_uuid")}")
            }

            if (project.hasProperty("mc_username")) {
                programArg("--username=${project.findProperty("mc_username")}")
            }
        }

        named("server") {
            server()
            configName = "Fabric Server"
            runDir("run/server")
        }

        create("datagen") {
            client()
            configName = "Fabric Datagen"

            source(sourceSets["main"])

            property("fabric-api.datagen")
            property("fabric-api.datagen.strict-validation", "true") // '--all' sets '--validate' to true as well
            property("fabric-api.datagen.output-dir", file("src/main/generated").absolutePath)
            property("sparkweave.datagen.mods", modId)
            runDir("build/datagen")
        }

        configureEach {
            isIdeConfigGenerated = false
            appendProjectPathToConfigName = false
            property("sparkweave.debug", "true")
            property("mixin.debug", "true")

            makeRunDir()

            // register runs as Gradle runs instead, so we can enable Gradle's config cache
            // see https://github.com/FabricMC/fabric-loom/issues/1349
            rootProject.idea.project.settings.runConfigurations.create<org.jetbrains.gradle.ext.Gradle>(configName) {
                taskNames = listOf(LoomTasks.getRunConfigTaskName(this@configureEach))
                setProject(project)
            }
        }
    }
}
