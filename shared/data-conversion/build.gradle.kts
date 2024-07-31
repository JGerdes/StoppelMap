plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.serialization)
}


kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "17"
        }
    }
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = ProjectDefaults.KOTLIN_JVM_TARGET
            }
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.serialization.json.core)
            implementation(project(":shared:data"))
            implementation(project(":shared:dto"))
        }
    }
}

android {
    namespace = "com.jonasgerdes.stoppelmap.data.conversion"
    compileSdk = libs.versions.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = ProjectDefaults.JAVA_COMPATIBILITY_VERSION
        targetCompatibility = ProjectDefaults.JAVA_COMPATIBILITY_VERSION
    }
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }
}