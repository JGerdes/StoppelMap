import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "DataUpdate"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            // KMM
            implementation(libs.koin.core)
            implementation(libs.skie.annotations)
            api(libs.kmm.viewmodel)
            api(libs.moko.resources)

            implementation(libs.okio.core)
            implementation(libs.okio.assets)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kermit)
            implementation(libs.ktor.client.core)
            implementation(libs.kotlinx.serialization.json.core)
            implementation(libs.kotlinx.serialization.json.okio)
            implementation(libs.sqldelight.coroutines)
            implementation(libs.androidx.datastore.preferences.core)

            implementation(project(":shared:base"))
            implementation(project(":shared:dto"))
            implementation(project(":shared:data"))
            implementation(project(":shared:data-conversion"))
            implementation(project(":shared:resources"))
            implementation(project(":shared:network"))
        }
    }
}

android {
    namespace = "com.jonasgerdes.stoppelmap.shared.dataupdate"
    compileSdk = libs.versions.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = ProjectDefaults.JAVA_COMPATIBILITY_VERSION
        targetCompatibility = ProjectDefaults.JAVA_COMPATIBILITY_VERSION
    }
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }
}

