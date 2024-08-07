plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.skie)
}


kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = ProjectDefaults.KOTLIN_JVM_TARGET
            }
        }
    }
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Home"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            // KMM
            implementation(libs.koin.core)
            implementation(libs.skie.annotations)
            api(libs.kmm.viewmodel)

            implementation(libs.kotlinx.datetime)
            implementation(libs.ktor.client.core)

            implementation(project(":shared:base"))
            implementation(project(":shared:resources"))
            implementation(project(":shared:dto"))
            implementation(project(":shared:data"))
            implementation(project(":shared:feature:countdown"))
            implementation(project(":shared:feature:data-update"))
            implementation(project(":shared:feature:schedule"))
        }
    }
}

android {
    namespace = "com.jonasgerdes.stoppelmap.shared.home"
    compileSdk = libs.versions.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = ProjectDefaults.JAVA_COMPATIBILITY_VERSION
        targetCompatibility = ProjectDefaults.JAVA_COMPATIBILITY_VERSION
    }
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }
}
