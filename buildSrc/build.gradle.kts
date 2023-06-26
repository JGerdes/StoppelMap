val kotlinVersion = "1.7.0"


plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
}

dependencies {
    implementation(libs.android.build.tools)
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.kotlin.stdlib)
}
