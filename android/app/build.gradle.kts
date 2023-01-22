import Git.commitSha

plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdk = SdkVersions.compileSdk
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
        minSdk = SdkVersions.minSdk
        targetSdk = SdkVersions.targetSdk
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
        kotlinCompilerExtensionVersion = DependencyVersions.compose
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

    with(DependencyVersions) {
        implementation("androidx.core:core-ktx:$androidxCore")
        implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinxDatetime")
        coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:$desugarSDK")

        implementation("androidx.compose.ui:ui:$compose")
        implementation("androidx.compose.ui:ui-tooling-preview:$compose")
        implementation("androidx.compose.material3:material3:$material3compose")
        implementation("androidx.compose.material:material-icons-extended:$compose")

        implementation("androidx.lifecycle:lifecycle-runtime-ktx:$androidxLifecycle")
        implementation("androidx.lifecycle:lifecycle-runtime-compose:$androidxLifecycle")
        implementation("androidx.activity:activity-compose:$androidxActivityCompose")
        implementation("androidx.navigation:navigation-compose:$androidxNavigation")

        implementation("io.insert-koin:koin-android:$koin")
        implementation("io.insert-koin:koin-androidx-compose:$koin")

        implementation("com.jakewharton.timber:timber:$timber")

        implementation("com.airbnb.android:lottie-compose:$lottie")

        implementation("com.squareup.sqldelight:android-driver:$sqldelight")

        implementation("com.google.android.play:app-update-ktx:$playInAppUpdate")


        debugImplementation("androidx.compose.ui:ui-tooling:$compose")

        testImplementation("junit:junit:4.13.2")
        androidTestImplementation("androidx.test.ext:junit:1.1.3")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    }
}