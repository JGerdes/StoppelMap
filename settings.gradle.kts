// repositoriesMode etc. is marked unstable
@file:Suppress("UnstableApiUsage")

include(":theme")
include(":feature-countdown")
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
    }
}
rootProject.name = "StoppelMap"
