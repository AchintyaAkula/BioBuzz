@file:Suppress("UnstableApiUsage")

import java.net.URL

pluginManagement {
	repositories {
		gradlePluginPortal()
		mavenCentral()
		google()
		maven("https://repo.dairy.foundation/releases")
	}
}

val versionCatalogList: List<String> = listOf("pedro", "sdk", "dairy")

dependencyResolutionManagement {
	repositories {
		google()
		mavenCentral()
	}
	versionCatalogs {
		val catalogTable: Map<File, String> = downloadCatalogs(versionCatalogList)
		for (f in catalogTable.keys) {
			create(f.name.dropLast(14)) {
				from(files(f))
			}
		}
	}
}

fun downloadCatalogs(files: List<String>): Map<File, String> {
	try {
		val table = files.associate { s ->
			file("./versions/$s.versions.toml") to "https://raw.githubusercontent.com/AchintyaAkula/FTCatalog/refs/heads/main/$s.versions.toml"
		}

		table.forEach { (file, string) ->
			URL(string).openConnection().apply { connectTimeout = 5000; readTimeout = 5000; useCaches = false; }
				.getInputStream().use { input ->
					file.parentFile.mkdirs()
					file.outputStream().use { output -> input.copyTo(output) }
				}
		}
		return table
	} catch (e: Exception) {
		throw IllegalStateException("Smth went wrong")
	}
}