import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.skie)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.sqldelight)
}


kotlin {
    android {
        namespace = "com.jonasgerdes.stoppelmap.shared.news"
        compileSdk = libs.versions.compileSdk.get().toInt()
        minSdk = libs.versions.minSdk.get().toInt()
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
        androidResources { enable = true }
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
            implementation(libs.kotlinx.serialization.json.core)
            implementation(libs.sqldelight.coroutines)
            implementation(libs.androidx.datastore.preferences.core)

            implementation(project(":shared:base"))
            implementation(project(":shared:dto"))
            implementation(project(":shared:network"))
            implementation(project(":shared:resources"))
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
