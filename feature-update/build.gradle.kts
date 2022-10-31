plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.jonasgerdes.stoppelmap.update"
    compileSdk = 32

    defaultConfig {
        minSdk = 21
        targetSdk = 32

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
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
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = DependencyVersions.compose
    }
}

dependencies {
    implementation(project(":base"))
    implementation(project(":theme"))

    with(DependencyVersions) {
        implementation("androidx.core:core-ktx:$androidxCore")
        coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:$desugarSDK")

        implementation("androidx.compose.ui:ui:$compose")
        implementation("androidx.compose.ui:ui-tooling-preview:$compose")
        implementation("androidx.compose.material3:material3:$material3compose")
        implementation("androidx.compose.material:material-icons-extended:$compose")

        implementation("androidx.lifecycle:lifecycle-runtime-ktx:$androidxLifecycle")
        implementation("androidx.lifecycle:lifecycle-runtime-compose:$androidxLifecycle")
        implementation("androidx.activity:activity-compose:$androidxActivityCompose")

        implementation("io.insert-koin:koin-android:$koin")
        implementation("io.insert-koin:koin-androidx-compose:$koin")

        implementation("com.jakewharton.timber:timber:$timber")

        implementation("com.google.android.play:app-update-ktx:$playInAppUpdate")

        debugImplementation("androidx.compose.ui:ui-tooling:$compose")
    }
}
