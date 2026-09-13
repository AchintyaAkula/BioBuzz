@file:Suppress("UnstableApiUsage")

pluginManagement {
	repositories {
		gradlePluginPortal()
		mavenCentral()
		google()
		maven("https://repo.dairy.foundation/releases")
	}
}
dependencyResolutionManagement {
	repositories {
		google()
		mavenCentral()
	}
	versionCatalogs {
		create("libs") {
			from(files("./versions/libs.versions.toml"))
		}
	}
}