plugins {
    id(Plugins.ANDROID_LIBRARY)
    id(Plugins.COMPOSE)
}

android {
    namespace = "com.jonasgerdes.stoppelmap.transportation"
}

dependencies {
    implementation(project(":base"))
    implementation(project(":theme"))

    with(DependencyVersions) {
        implementation("androidx.core:core-ktx:$androidxCore")
        implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinxDatetime")
        coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:$desugarSDK")
        
        implementation("com.google.accompanist:accompanist-pager:${accompoanist}")

        implementation("androidx.lifecycle:lifecycle-runtime-ktx:$androidxLifecycle")
        implementation("androidx.lifecycle:lifecycle-runtime-compose:$androidxLifecycle")
        implementation("androidx.activity:activity-compose:$androidxActivityCompose")

        implementation("io.insert-koin:koin-android:$koin")
        implementation("io.insert-koin:koin-androidx-compose:$koin")

        implementation("com.jakewharton.timber:timber:$timber")


        debugImplementation("androidx.compose.ui:ui-tooling:$compose")
    }
}
