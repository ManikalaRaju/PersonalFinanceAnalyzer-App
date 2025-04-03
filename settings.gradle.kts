pluginManagement {
    plugins {
        id("org.jetbrains.kotlin.android") version "2.0.21" // or whatever Kotlin version you're using
        id("org.jetbrains.kotlin.kapt") version "2.0.21"
        id("org.jetbrains.kotlin.plugin.compose") version "2.0.21"
    }
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        kotlin("jvm") version "2.0.21"
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
    }
}


rootProject.name = "Personal Finance Analyzer"
include(":app")
