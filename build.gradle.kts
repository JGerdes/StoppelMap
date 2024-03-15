plugins {
    id(libs.plugins.kotlin.multiplatform.get().pluginId) apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
