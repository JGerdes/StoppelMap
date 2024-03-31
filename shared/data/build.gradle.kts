plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.android.library)
}

sqldelight {
    databases {
        create("StoppelMapDatabase") {
            packageName.set("com.jonasgerdes.stoppelmap.data")
            srcDirs.setFrom("src/commonMain/sqldelight")
            schemaOutputDirectory =
                file("src/main/sqldelight/com/jonasgerdes/stoppelmap/data/schema")
            version = 1
            verifyMigrations = true
        }
    }
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
            implementation(libs.koin.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.koin.core)
        }

        jvmMain.dependencies {
            implementation(libs.sqldelight.driver.sqlite)
        }

        androidMain.dependencies {
            implementation(libs.sqldelight.driver.android)
        }

        iosMain.dependencies {
            implementation(libs.sqldelight.driver.native)
            implementation("co.touchlab:stately-common:2.0.6")
            implementation("co.touchlab:stately-concurrency:2.0.6")
        }
    }
}

android {
    namespace = "com.jonasgerdes.stoppelmap.data"
    compileSdk = libs.versions.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = ProjectDefaults.JAVA_COMPATIBILITY_VERSION
        targetCompatibility = ProjectDefaults.JAVA_COMPATIBILITY_VERSION
    }
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }
}