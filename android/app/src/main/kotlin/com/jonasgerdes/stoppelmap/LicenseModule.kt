package com.jonasgerdes.stoppelmap

import com.jonasgerdes.stoppelmap.settings.data.ImageSource
import com.jonasgerdes.stoppelmap.settings.data.ImageSources
import com.jonasgerdes.stoppelmap.settings.data.Libraries
import com.jonasgerdes.stoppelmap.settings.data.Library
import com.jonasgerdes.stoppelmap.settings.data.License
import org.koin.dsl.module

val licenseModule = module {

    factory {
        Libraries(
            listOf(
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
                    githubUrl = "https://github.com/airbnb/lottie-android"
                ),
                Library(
                    name = "Kotlin, Kotlin Coroutines",
                    author = "JetBrains, Inc.",
                    license = License.Apache2(),
                    githubUrl = "https://github.com/JetBrains/kotlin"
                ),
                Library(
                    name = "KotlinX DateTime",
                    author = "JetBrains, Inc.",
                    license = License.Apache2(),
                    githubUrl = "https://github.com/Kotlin/kotlinx-datetime"
                ),
                Library(
                    name = "Koin",
                    author = "Kotzilla, Koin Contributors",
                    license = License.Apache2(),
                    githubUrl = "https://github.com/InsertKoinIO/koin"
                ),
                Library(
                    name = "Mapbox",
                    author = "Mapbox Inc.",
                    license = License.BSD2Clause(),
                    githubUrl = "https://github.com/mapbox/mapbox-maps-android"
                ),
                Library(
                    name = "SQLDelight",
                    author = "CashApp",
                    license = License.Apache2(),
                    githubUrl = "https://github.com/cashapp/sqldelight"
                ),
                Library(
                    name = "Timber",
                    author = "JakeWharton, Timber Contributors",
                    license = License.Apache2(),
                    githubUrl = "https://github.com/JakeWharton/timber"
                ),
            )
        )
    }

    factory {
        ImageSources(
            listOf(
                ImageSource(
                    author = "Erwan Hesry",
                    work = "Feuerwerk",
                    sourceUrl = "https://unsplash.com/photos/WPTHZkA-M4I",
                    license = License.Unsplash(),
                    website = "https://unsplash.com/@erwanhesry",
                    resource = R.drawable.fireworks
                )
            )
        )
    }
}
