plugins {
    id(Plugins.ANDROID_LIBRARY)
    id(Plugins.COMPOSE)
}

android {
    namespace = "com.jonasgerdes.stoppelmap.theme"
}

dependencies {
    // Needed for setting theme
    implementation(libs.material)

    implementation(libs.androidx.core.ktx)
}
