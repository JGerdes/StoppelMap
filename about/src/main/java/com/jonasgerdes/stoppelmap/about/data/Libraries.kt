package com.jonasgerdes.stoppelmap.about.data

import com.jonasgerdes.stoppelmap.about.view.LibraryCard
import com.jonasgerdes.stoppelmap.about.view.License

internal val libraries = listOf(
    LibraryCard(
        name = "Android Architecture Components",
        author = "The Android Open Source Project",
        license = License.Apache2(),
        sourceUrl = "https://android.googlesource.com/platform/frameworks/support/+/master/lifecycle/common/src/main/java/android/arch"
    ),
    LibraryCard(
        name = "Android Compatibility Library v7",
        author = "The Android Open Source Project",
        license = License.Apache2(),
        sourceUrl = "https://android.googlesource.com/platform/frameworks/support.git/+/master/v7/"
    ),
    LibraryCard(
        name = "Android Design Support Library",
        author = "The Android Open Source Project",
        license = License.Apache2(),
        sourceUrl = "https://android.googlesource.com/platform/frameworks/support/+/master/design/"
    ),
    LibraryCard(
        name = "Android Constraint Layout Support Library",
        author = "The Android Open Source Project",
        license = License.Apache2(),
        sourceUrl = "https://android.googlesource.com/platform/frameworks/opt/sherpa/+/studio-3.0/constraintlayout"
    ),
    LibraryCard(
        name = "Firebase Core, Firebase Messaging",
        author = "The Android Open Source Project",
        license = License.Apache2(),
        sourceUrl = "https://android.googlesource.com/platform/frameworks/opt/sherpa/+/studio-3.0/constraintlayout"
    ),
    LibraryCard(
        name = "Groupie",
        author = "Lisa Wray",
        license = License.MIT(),
        githubUrl = "https://github.com/lisawray/groupie"
    ),
    LibraryCard(
        name = "Retrofit",
        author = "Square, Inc.",
        license = License.Apache2(),
        githubUrl = "https://github.com/square/retrofit"
    ),
    LibraryCard(
        name = "OkHttp",
        author = "Square, Inc.",
        license = License.Apache2(),
        githubUrl = "https://github.com/square/okhttp"
    ),
    LibraryCard(
        name = "Moshi",
        author = "Square, Inc.",
        license = License.Apache2(),
        githubUrl = "https://github.com/square/moshi"
    ),
    LibraryCard(
        name = "Glide",
        author = "Google, Inc.;Jake Wharton;Xcellent Creations, Inc.;Anthony Dekker;Android Open Source Project",
        license = License(
            name = "BSD, part MIT and Apache 2.0",
            link = "https://github.com/bumptech/glide/blob/master/LICENSE"
        ),
        githubUrl = "https://github.com/google/gson"
    ),
    LibraryCard(
        name = "Mapbox Android SDK",
        author = "Mapbox",
        license = License(
            name = "Mapbox License",
            link = "https://github.com/mapbox/mapbox-gl-native/blob/master/LICENSE.md"
        ),
        githubUrl = "https://github.com/mapbox/mapbox-gl-native"
    ),
    LibraryCard(
        name = "ThreeTenABP",
        author = "Jake Wharton",
        license = License.Apache2(),
        githubUrl = "https://github.com/JakeWharton/ThreeTenABP"
    ),
    LibraryCard(
        name = "Lottie",
        author = "Airbnb, Inc.",
        license = License.Apache2(),
        githubUrl = "https://github.com/airbnb/lottie-android"
    ),
    LibraryCard(
        name = "Kotlin, Kotlin Coroutines",
        author = "JetBrains, Inc.",
        license = License.Apache2(),
        githubUrl = "https://github.com/JetBrains/kotlin"
    ),
    LibraryCard(
        name = "Koin",
        author = "JetBrains, Inc.",
        license = License.Apache2(),
        githubUrl = "https://github.com/InsertKoinIO/kotlin"
    )
)
