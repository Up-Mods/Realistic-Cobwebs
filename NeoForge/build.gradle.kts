import dev.upcraft.gradle.multiloader.applyMcGradleConventions
import org.gradle.internal.extensions.stdlib.capitalized

plugins {
	id("dev.upcraft.gradle.multiloader.multiloader-loader")
    id("net.neoforged.moddev")
}
applyMcGradleConventions("neoforge")

val modId = providers.gradleProperty("mod_id").get()

// need this before dependencies because it configures the plugin and creates additionalRuntimeClasspath configuration
neoForge.version = libs.versions.neoforge.get()

val localRuntime by configurations.dependencyScope("localRuntime")
configurations.runtimeClasspath.configure { extendsFrom(localRuntime) }

dependencies {
	interfaceInjectionData(project(":Realistic-Cobwebs-Common"))
	accessTransformers(project(":Realistic-Cobwebs-Common"))
	compileOnly(libs.autoservice.annotations)
	annotationProcessor(libs.autoservice)

	implementation(libs.resourcefulconfig.neoforge)

//	compileOnly(variantOf(libs.emi.neoforge) {
//		classifier("api")
//	})
//	localRuntime(libs.emi.neoforge)

	testImplementation(libs.neoforge.testframework)
}

neoForge {
	mods {
		// define mod <-> source bindings
		// these are used to tell the game which sources are for which mod
		// mostly optional in a single mod project
		// but multi mod projects should define one per mod
        register(modId) {
			sourceSet(sourceSets["main"])
			sourceSet(project(":Realistic-Cobwebs-Common").sourceSets["main"])
		}
	}

	unitTest {
		enable()

		testedMod = mods[modId]
		loadedMods = listOf(mods[modId])
	}

    runs {
        register("client") {
            client()
            devLogin = true
            gameDirectory = file("run/client")
            systemProperty("neoforge.enabledGameTestNamespaces", modId)

            sourceSet = sourceSets["main"]
            loadedMods = listOf(mods[modId])
        }

        register("server") {
            server()
            gameDirectory = file("run/server")
            systemProperty("neoforge.enabledGameTestNamespaces", modId)

            sourceSet = sourceSets["main"]
            loadedMods = listOf(mods[modId])

            programArgument("--nogui")
        }

        register("data") {
            clientData()
            gameDirectory = file("run/data")

            programArguments.addAll(
                "--mod", "sparkweave",
                "--mod", modId,
                "--all",
                "--flat",
                "--output", file("src/main/generated").absolutePath,
                "--existing", file("src/main/resources").absolutePath
            )
            sourceSet = sourceSets["main"]
            loadedMods = listOf(mods[modId])
        }

        configureEach {
            logLevel = org.slf4j.event.Level.DEBUG
            systemProperty("forge.logging.markers", "REGISTRIES")
            systemProperty("sparkweave.debug", "true")

            ideName = "NeoForge ${name.capitalized()}"
        }
    }
}

sourceSets["main"].resources { srcDir("src/main/generated") }
