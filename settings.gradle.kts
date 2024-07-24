// repositoriesMode etc. is marked unstable
@file:Suppress("UnstableApiUsage")

// Shared
include(":shared:base")
include(":shared:resources")
include(":shared:data")
include(":shared:dto")
include(":shared:data-conversion")
include(":shared:network")
include(":shared:feature:home")
include(":shared:feature:licenses")
include(":shared:feature:data-update")
include(":shared:feature:countdown")
include(":shared:feature:schedule")
include(":shared:feature:transportation")
include(":shared:feature:news")
include(":shared:app")
include(":shared")

// Common
include(":base")

// Android
include(":android:theme")
include(":android:feature-settings")
include(":android:feature-countdown")
include(":android:feature-transportation")
include(":android:feature-map")
include(":android:feature-schedule")
include(":android:feature-news")
include(":android:feature-update")
include(":android:app")

// Server
include(":server")

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
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "StoppelMap"
