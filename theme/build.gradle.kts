plugins {
    id(Plugins.ANDROID_LIBRARY)
    id(Plugins.COMPOSE)
}

android {
    namespace = "com.jonasgerdes.stoppelmap.theme"
}

dependencies {

    with(DependencyVersions) {
        // Needed for setting theme
        implementation("com.google.android.material:material:$material")

        implementation("androidx.core:core-ktx:$androidxCore")
        
        debugImplementation("androidx.compose.ui:ui-tooling:$compose")
    }
}
