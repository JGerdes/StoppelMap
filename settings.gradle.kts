// repositoriesMode etc. is marked unstable
@file:Suppress("UnstableApiUsage")

import java.io.FileInputStream
import java.util.Properties

// Shared
include(":shared")

// Common
include(":base")
include(":data")

// Android
include(":android:theme")
include(":android:feature-settings")
include(":android:feature-countdown")
include(":android:feature-transportation")
include(":android:feature-map")
include(":android:feature-schedule")
include(":android:feature-news")
include(":android:feature-update")
include(":android:feature-data-update")
include(":android:app")

// Server
include(":server:api")

// Preparation
include(":preparation")


pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://api.mapbox.com/downloads/v2/releases/maven") {
            authentication {
                create<BasicAuthentication>("basic")
            }
            FileInputStream(
                File(rootProject.projectDir, "secrets.properties")
            ).use {
                val properties = Properties()
                properties.load(it)
                credentials {
                    username = "mapbox"
                    password = properties["mapBoxDownloadToken"] as String
                }
            }
        }
    }
}
rootProject.name = "StoppelMap"
