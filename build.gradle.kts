plugins {
    alias(libs.plugins.sqldelight) apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
