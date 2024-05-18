package com.jonasgerdes.stoppelmap.licenses.data

import com.jonasgerdes.stoppelmap.licenses.model.Library
import com.jonasgerdes.stoppelmap.licenses.model.License

internal val commonLibraries = listOf(
    Library(
        name = "Accompanist",
        author = "Google, Chris Banes, Accompanist Contributors",
        license = License.Apache2(),
        sourceUrl = "https://github.com/google/accompanist"
    ),
    Library(
        name = "Android Jetpack",
        author = "The Android Open Source Project",
        license = License.Apache2(),
        sourceUrl = "https://android.googlesource.com/platform/frameworks/support/+/androidx-master-dev"
    ),
    Library(
        name = "Android Jetpack Compose and Compose Material 3",
        author = "The Android Open Source Project",
        license = License.Apache2(),
        sourceUrl = "https://android.googlesource.com/platform/frameworks/support/+/androidx-master-dev/compose/"
    ),
    Library(
        name = "Lottie",
        author = "Airbnb, Inc., Lottie Contributors",
        license = License.Apache2(),
        sourceUrl = "https://github.com/airbnb/lottie-android"
    ),
    Library(
        name = "Kotlin, Kotlin Coroutines, Kotlin Multiplatform",
        author = "JetBrains, Inc.",
        license = License.Apache2(),
        sourceUrl = "https://github.com/JetBrains/kotlin"
    ),
    Library(
        name = "KotlinX DateTime",
        author = "JetBrains, Inc.",
        license = License.Apache2(),
        sourceUrl = "https://github.com/Kotlin/kotlinx-datetime"
    ),
    Library(
        name = "Koin",
        author = "Kotzilla, Koin Contributors",
        license = License.Apache2(),
        sourceUrl = "https://github.com/InsertKoinIO/koin"
    ),
    Library(
        name = "MapLibre",
        author = "MapLibre contributors",
        license = License.BSD2Clause(),
        sourceUrl = "https://github.com/maplibre/maplibre-native"
    ),
    Library(
        name = "SQLDelight",
        author = "CashApp",
        license = License.Apache2(),
        sourceUrl = "https://github.com/cashapp/sqldelight"
    ),
    Library(
        name = "Timber",
        author = "JakeWharton, Timber Contributors",
        license = License.Apache2(),
        sourceUrl = "https://github.com/JakeWharton/timber"
    ),
)