plugins {
    id(Plugins.ANDROID_LIBRARY)
    id(Plugins.COMPOSE)
}

android {
    namespace = "com.jonasgerdes.stoppelmap.settings"
}

dependencies {
    implementation(project(":base"))
    implementation(project(":android:theme"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.datetime)
    coreLibraryDesugaring(libs.android.desugar)
    implementation(libs.bundles.androidx.runtime)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.datastore)
    implementation(libs.bundles.koin.compose)
    implementation(libs.timber)
    implementation(libs.accompanist.pager)
    implementation(libs.google.play.app.update)
}
