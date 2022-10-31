plugins {
    id(Plugins.ANDROID_LIBRARY)
    id(Plugins.COMPOSE)
}

android {
    namespace = "com.jonasgerdes.stoppelmap.update"
}

dependencies {
    implementation(project(":base"))
    implementation(project(":theme"))

    with(DependencyVersions) {
        implementation("androidx.core:core-ktx:$androidxCore")
        coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:$desugarSDK")

        implementation("androidx.lifecycle:lifecycle-runtime-ktx:$androidxLifecycle")
        implementation("androidx.lifecycle:lifecycle-runtime-compose:$androidxLifecycle")
        implementation("androidx.activity:activity-compose:$androidxActivityCompose")

        implementation("io.insert-koin:koin-android:$koin")
        implementation("io.insert-koin:koin-androidx-compose:$koin")

        implementation("com.jakewharton.timber:timber:$timber")

        implementation("com.google.android.play:app-update-ktx:$playInAppUpdate")

        debugImplementation("androidx.compose.ui:ui-tooling:$compose")
    }
}
