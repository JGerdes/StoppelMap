plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.squareup.sqldelight")
}

android {
    namespace = "com.jonasgerdes.stoppelmap.data"
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

sqldelight {
    database("StoppelMapDatabase") {
        packageName = "com.jonasgerdes.stoppelmap.data"
        sourceFolders = listOf("sqldelight")
        schemaOutputDirectory = file("src/main/sqldelight/com/jonasgerdes/stoppelmap/data/schema")
        version = 1
        verifyMigrations = true
    }
}

dependencies {

    with(DependencyVersions) {
        implementation("androidx.core:core-ktx:$androidxCore")
        implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinxDatetime")
        coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:$desugarSDK")

        implementation("io.insert-koin:koin-android:$koin")
        implementation("io.insert-koin:koin-androidx-compose:$koin")

        implementation("com.squareup.sqldelight:android-driver:$sqldelight")
        implementation("com.squareup.sqldelight:coroutines-extensions-jvm:$sqldelight")

        implementation("com.jakewharton.timber:timber:$timber")
    }
}
