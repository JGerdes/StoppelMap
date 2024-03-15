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
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))

    implementation(libs.android.build.tools)
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.datetime)
}
