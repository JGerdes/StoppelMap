plugins {
    id(Plugins.ANDROID_LIBRARY)
    id(Plugins.COMPOSE)
    kotlin("plugin.serialization") version DependencyVersions.kotlin
}

android {
    namespace = "com.jonasgerdes.stoppelmap.news"
}

dependencies {
    implementation(project(":base"))
    implementation(project(":theme"))
    implementation(project(":data"))

    with(DependencyVersions) {
        implementation("androidx.core:core-ktx:$androidxCore")
        implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinxDatetime")
        coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:$desugarSDK")

        implementation("androidx.lifecycle:lifecycle-runtime-ktx:$androidxLifecycle")
        implementation("androidx.lifecycle:lifecycle-runtime-compose:$androidxLifecycle")
        implementation("androidx.activity:activity-compose:$androidxActivityCompose")

        implementation("io.insert-koin:koin-android:$koin")
        implementation("io.insert-koin:koin-androidx-compose:$koin")

        implementation("com.google.accompanist:accompanist-pager:${accompoanist}")
        implementation("io.coil-kt:coil-compose:$coil")

        implementation("io.ktor:ktor-client-android:$ktor")
        implementation("io.ktor:ktor-client-logging:$ktor")
        implementation("io.ktor:ktor-client-content-negotiation:$ktor")
        implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationJson")

        implementation("com.jakewharton.timber:timber:$timber")

        debugImplementation("androidx.compose.ui:ui-tooling:$compose")
    }
}
