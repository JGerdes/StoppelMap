import Git.getCommit

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
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
        val version = loadVersions()
        versionCode = version.code
        versionName = "${version.name}-${commit.shortSha}"

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
            versionNameSuffix = "-debug"
        }

        getByName("release") {
            isMinifyEnabled = ProjectDefaults.MINIFY_RELEASE
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

    implementation(project(":shared:resources"))
    implementation(project(":shared:base"))
    implementation(project(":shared:feature:home"))
    implementation(project(":shared:feature:licenses"))
    implementation(project(":shared:feature:countdown"))
    implementation(project(":shared:feature:schedule"))
    implementation(project(":shared:feature:map"))
    implementation(project(":shared:feature:transportation"))
    implementation(project(":shared:feature:news"))
    implementation(project(":shared:app"))
    implementation(project(":base"))
    implementation(project(":shared:data"))
    implementation(project(":shared:dto"))


    implementation(project(":android:theme"))
    implementation(project(":android:feature-settings"))
    implementation(project(":android:feature-countdown"))
    implementation(project(":android:feature-map"))
    implementation(project(":android:feature-schedule"))
    implementation(project(":android:feature-transportation"))
    implementation(project(":android:feature-news"))
    implementation(project(":android:feature-update"))
    implementation(project(":shared:feature:data-update"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.datetime)

    implementation(libs.compose.ui.core)
    implementation(libs.compose.ui.preview)
    implementation(libs.compose.material.components)
    implementation(libs.compose.material.icons)
    implementation(libs.glance.appwidget)
    implementation(libs.bundles.androidx.runtime)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.splashscreen)
    implementation(libs.bundles.koin.compose)
    implementation(libs.timber)
    implementation(libs.sqldelight.driver.android)
    implementation(libs.google.play.app.update)
    implementation(libs.maplibre)

    implementation(libs.okio.assets)

    debugImplementation(libs.compose.ui.tooling)
}
