// repositoriesMode etc. is marked unstable
@file:Suppress("UnstableApiUsage")

// Shared
include(":shared:base")
include(":shared:resources")
include(":shared:feature:countdown")
include(":shared:app")
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
    }
}
rootProject.name = "StoppelMap"
