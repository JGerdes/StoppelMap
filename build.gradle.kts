plugins {
    alias(libs.plugins.sqldelight) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.ktor) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
