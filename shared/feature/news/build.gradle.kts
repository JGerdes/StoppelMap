plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.skie)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.sqldelight)
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
            baseName = "News"
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
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.sqldelight.coroutines)
            implementation(libs.androidx.datastore.preferences.core)

            implementation(project(":shared:base"))
            implementation(project(":shared:network"))
            implementation(project(":shared:resources"))
        }

        androidMain.dependencies {
            implementation(libs.sqldelight.driver.android)
        }

        iosMain.dependencies {
            implementation(libs.sqldelight.driver.native)
        }
    }
}

sqldelight {
    databases {
        create("NewsDatabase") {
            packageName.set("com.jonasgerdes.stoppelmap.news.database.model")
            srcDirs.setFrom("src/commonMain/sqldelight")
            schemaOutputDirectory =
                file("src/commonMain/sqldelight/com/jonasgerdes/stoppelmap/news/schema")
            version = 1
            verifyMigrations = true
        }
    }
}

android {
    namespace = "com.jonasgerdes.stoppelmap.shared.news"
    compileSdk = libs.versions.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = ProjectDefaults.JAVA_COMPATIBILITY_VERSION
        targetCompatibility = ProjectDefaults.JAVA_COMPATIBILITY_VERSION
    }
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }
}
