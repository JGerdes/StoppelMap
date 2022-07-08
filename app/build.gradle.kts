import Git.commitSha

plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdk = SdkVersions.compileSdk
    namespace = "com.jonasgerdes.stoppelmap"

    defaultConfig {
        applicationId = "com.jonasgerdes.stoppelmap"
        minSdk = SdkVersions.minSdk
        targetSdk = SdkVersions.targetSdk
        versionCode = 43
        versionName = "v2022.0.0-alpha-$commitSha"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = freeCompilerArgs + listOf("-Xopt-in=kotlin.RequiresOptIn")
    }

    signingConfigs {
        getByName("debug") {
            storeFile = file("../debug.keystore")
        }
    }
}

dependencies {
    with(DependencyVersions) {
        implementation("androidx.core:core-ktx:$androidxCore")

        testImplementation("junit:junit:4.13.2")
        androidTestImplementation("androidx.test.ext:junit:1.1.3")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    }
}
