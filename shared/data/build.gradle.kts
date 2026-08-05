import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.android.kotlin.multiplatform.library)
}

sqldelight {
    databases {
        create("StoppelMapDatabase") {
            packageName.set("com.jonasgerdes.stoppelmap.data")
            srcDirs.setFrom("src/commonMain/sqldelight")
            schemaOutputDirectory =
                file("src/commonMain/sqldelight/com/jonasgerdes/stoppelmap/data/schema")
            version = 2
            verifyMigrations = true
        }
    }
}

kotlin {
    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
    android {
        namespace = "com.jonasgerdes.stoppelmap.data"
        compileSdk = libs.versions.compileSdk.get().toInt()
        minSdk = libs.versions.minSdk.get().toInt()
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
        androidResources { enable = true }
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
