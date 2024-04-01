import Git.getCommit
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.skie)
    alias(libs.plugins.buildkonfig)
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
            baseName = "MainApp"
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

            implementation(project(":shared:base"))
            implementation(project(":shared:data"))
            implementation(project(":shared:feature:countdown"))
            implementation(project(":shared:feature:data-update"))
        }

        iosMain.dependencies {
            implementation(libs.sqldelight.driver.native)
        }
    }
}

android {
    namespace = "com.jonasgerdes.stoppelmap.shared.app"
    compileSdk = libs.versions.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = ProjectDefaults.JAVA_COMPATIBILITY_VERSION
        targetCompatibility = ProjectDefaults.JAVA_COMPATIBILITY_VERSION
    }
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }
}

buildkonfig {
    packageName = "com.jonasgerdes.stoppelmap"
    exposeObjectWithName = "CommonBuildConfig"

    defaultConfigs {
        loadProperties(
            "./secrets.properties",
            onSuccess = { properties ->
                buildConfigField(
                    STRING,
                    "STOPPELMAP_API_KEY",
                    properties["stoppelMapApiKey"] as String,
                )
            },
            onFailure = { println("Unable to read secrets.properties") }
        )
        buildConfigField(STRING, "COMMIT_SHORT_SHA", getCommit().shortSha)
        buildConfigField(STRING, "COMMIT_SHA", getCommit().sha)
    }
}
