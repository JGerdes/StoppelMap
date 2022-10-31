plugins {
    id(Plugins.ANDROID_LIBRARY)
    id(Plugins.COMPOSE)
}

android {
    namespace = "com.jonasgerdes.stoppelmap.map"
}

dependencies {
    implementation(project(":data"))
    implementation(project(":theme"))

    with(DependencyVersions) {
        implementation("androidx.core:core-ktx:$androidxCore")
        implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinxDatetime")
        coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:$desugarSDK")

        implementation("androidx.lifecycle:lifecycle-runtime-ktx:$androidxLifecycle")
        implementation("androidx.lifecycle:lifecycle-runtime-compose:$androidxLifecycle")
        implementation("androidx.activity:activity-compose:$androidxActivityCompose")

        implementation("io.insert-koin:koin-android:$koin")
        implementation("io.insert-koin:koin-androidx-compose:$koin")

        implementation("com.jakewharton.timber:timber:$timber")
        implementation("com.mapbox.maps:android:$mapBox")
        implementation("com.google.android.gms:play-services-location:$playServiceLocation")

        implementation("com.squareup.sqldelight:coroutines-extensions-jvm:${sqldelight}")


        debugImplementation("androidx.compose.ui:ui-tooling:$compose")
    }
}
