// repositoriesMode etc. is marked unstable
@file:Suppress("UnstableApiUsage")

include(":data")

include(":preparation")

include(":theme")
include(":feature-countdown")
include(":feature-transportation")
include(":feature-map")
include(":app")


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
            java.io.FileInputStream(File("secrets.properties")).use {
                val properties = java.util.Properties()
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
