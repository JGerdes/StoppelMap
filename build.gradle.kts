import java.util.Properties

plugins {
    alias(libs.plugins.sqldelight) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.ktor) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.moko.resources) apply false
    alias(libs.plugins.kotlin.compose) apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

task("updateVersion") {
    group = "versioning"
    doFirst {
        val versionFile = File(project.rootDir, "version.properties").also {
            if (!it.exists()) it.createNewFile()
        }
        val versionProperties =
            Properties().also {
                it.load(versionFile.inputStream())
            }
        val version = generateVersions()

        versionProperties.setProperty("code", version.code.toString())
        versionProperties.setProperty("semanticName", version.semanticName)
        versionProperties.setProperty("name", version.name)

        versionProperties.store(versionFile.outputStream(), null)
    }
}