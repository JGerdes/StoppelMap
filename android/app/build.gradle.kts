import Git.commitSha

plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()
    namespace = "com.jonasgerdes.stoppelmap"

    signingConfigs {
        getByName("debug") {
            storeFile = file("../debug.keystore")
        }

        create("release") {
            loadProperties(
                "./android/signing.properties",
                onSuccess = { properties ->
                    storeFile = File(projectDir, properties["keystorePath"] as String)
                    storePassword = properties["keystorePassword"] as String
                    keyAlias = properties["keyAlias"] as String
                    keyPassword = properties["keyPassword"] as String
                },
                onFailure = { println("Unable to read signing.properties") }
            )
        }
    }

    defaultConfig {
        applicationId = "com.jonasgerdes.stoppelmap"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 57
        versionName = "v2022.10.31.01-$commitSha"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
            isDebuggable = true
            applicationIdSuffix = ".debug"
        }

        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
            isDebuggable = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8

        isCoreLibraryDesugaringEnabled = true
    }

    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = freeCompilerArgs + listOf("-Xopt-in=kotlin.RequiresOptIn")
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.get()
    }
}

dependencies {

    implementation(project(":base"))
    implementation(project(":data"))

    implementation(project(":android:theme"))
    implementation(project(":android:feature-countdown"))
    implementation(project(":android:feature-map"))
    implementation(project(":android:feature-schedule"))
    implementation(project(":android:feature-transportation"))
    implementation(project(":android:feature-news"))
    implementation(project(":android:feature-update"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.datetime)
    coreLibraryDesugaring(libs.android.desugar)

    implementation(libs.compose.ui.core)
    implementation(libs.compose.ui.preview)
    implementation(libs.compose.material.components)
    implementation(libs.compose.material.icons)
    implementation(libs.bundles.androidx.runtime)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.bundles.koin.compose)
    implementation(libs.timber)
    implementation(libs.lottie.compose)
    implementation(libs.sqldelight.driver.android)
    implementation(libs.google.play.app.update)

    debugImplementation(libs.compose.ui.tooling)
}
