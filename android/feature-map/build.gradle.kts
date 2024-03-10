plugins {
    id(Plugins.ANDROID_LIBRARY)
    id(Plugins.COMPOSE)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.jonasgerdes.stoppelmap.map"
}

dependencies {
    implementation(project(":base"))
    implementation(project(":data"))
    implementation(project(":android:theme"))
    implementation(project(":android:feature-settings"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.datetime)
    implementation(libs.bundles.androidx.runtime)
    implementation(libs.androidx.activity.compose)
    implementation(libs.bundles.koin.compose)
    implementation(libs.timber)
    implementation(libs.mapbox)
    implementation(libs.google.play.services.location)
    implementation(libs.sqldelight.coroutines)
    implementation(libs.androidx.datastore)
    implementation(libs.kotlinx.serialization.json)

}
