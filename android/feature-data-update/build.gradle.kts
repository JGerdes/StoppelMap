plugins {
    id(Plugins.ANDROID_LIBRARY)
    id(Plugins.COMPOSE)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.jonasgerdes.stoppelmap.dataupdate"
}

dependencies {
    implementation(project(":base"))
    implementation(project(":android:theme"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.datetime)
    implementation(libs.bundles.androidx.runtime)
    implementation(libs.androidx.activity.compose)
    implementation(libs.bundles.koin.compose)
    implementation(libs.timber)
    implementation(libs.accompanist.pager)
    implementation(libs.bundles.ktor.android)
    implementation(libs.androidx.datastore)
}
