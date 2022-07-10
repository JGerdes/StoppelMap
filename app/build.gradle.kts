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
        kotlinCompilerExtensionVersion = DependencyVersions.compose
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
        implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinxDatetime")
        coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:$desugarSDK")

        implementation("androidx.compose.ui:ui:$compose")
        implementation("androidx.compose.ui:ui-tooling-preview:$compose")
        implementation("androidx.compose.material3:material3:$material3")
        implementation("androidx.compose.material:material-icons-extended:$compose")

        implementation("androidx.lifecycle:lifecycle-runtime-ktx:$androidxRuntime")
        implementation("androidx.activity:activity-compose:$androidxActivityCompose")
        implementation("androidx.navigation:navigation-compose:$androidxNavigation")

        implementation("io.insert-koin:koin-android:$koin")
        implementation("io.insert-koin:koin-androidx-compose:$koin")

        implementation("com.jakewharton.timber:timber:$timber")

        implementation("com.airbnb.android:lottie-compose:$lottie")


        debugImplementation("androidx.compose.ui:ui-tooling:$compose")

        testImplementation("junit:junit:4.13.2")
        androidTestImplementation("androidx.test.ext:junit:1.1.3")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    }
}
