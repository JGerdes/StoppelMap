plugins {
    id(Plugins.ANDROID_LIBRARY)
    id(Plugins.COMPOSE)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.jonasgerdes.stoppelmap.news"
}

dependencies {
    implementation(project(":base"))
    implementation(project(":android:theme"))
    implementation(project(":data"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.datetime)
    coreLibraryDesugaring(libs.android.desugar)
    implementation(libs.bundles.androidx.runtime)
    implementation(libs.androidx.activity.compose)
    implementation(libs.bundles.koin.compose)
    implementation(libs.timber)
    implementation(libs.accompanist.pager)
    implementation(libs.coil.compose)
    implementation(libs.bundles.ktor.android)
}
