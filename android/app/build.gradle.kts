import Git.getCommit

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
        val commit = getCommit()
        val version = getVersion(commit.shortSha)
        versionCode = version.code
        versionName = version.name

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        loadProperties(
            "./secrets.properties",
            onSuccess = { properties ->
                buildConfigField(
                    "String",
                    "STOPPELMAP_API_KEY",
                    "\"${properties["stoppelMapApiKey"]}\""
                )
            },
            onFailure = { println("Unable to read secrets.properties") }
        )

        buildConfigField("String", "COMMIT_SHA", "\"${commit.sha}\"")
    }

    buildTypes {
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
            isDebuggable = true
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
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
    }

    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = freeCompilerArgs + listOf("-Xopt-in=kotlin.RequiresOptIn")
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
}

dependencies {

    implementation(project(":base"))
    implementation(project(":data"))

    implementation(project(":android:theme"))
    implementation(project(":android:feature-settings"))
    implementation(project(":android:feature-countdown"))
    implementation(project(":android:feature-map"))
    implementation(project(":android:feature-schedule"))
    implementation(project(":android:feature-transportation"))
    implementation(project(":android:feature-news"))
    implementation(project(":android:feature-update"))
    implementation(project(":android:feature-data-update"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.datetime)

    implementation(libs.compose.ui.core)
    implementation(libs.compose.ui.preview)
    implementation(libs.compose.material.components)
    implementation(libs.compose.material.icons)
    implementation(libs.bundles.androidx.runtime)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.splashscreen)
    implementation(libs.bundles.koin.compose)
    implementation(libs.timber)
    implementation(libs.lottie.compose)
    implementation(libs.sqldelight.driver.android)
    implementation(libs.google.play.app.update)

    debugImplementation(libs.compose.ui.tooling)
}
