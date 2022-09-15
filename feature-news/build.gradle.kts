plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    kotlin("plugin.serialization") version DependencyVersions.kotlin
}

android {
    namespace = "com.jonasgerdes.stoppelmap.news"
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
    implementation(project(":data"))

    with(DependencyVersions) {
        implementation("androidx.core:core-ktx:$androidxCore")
        implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinxDatetime")
        coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:$desugarSDK")

        implementation("androidx.compose.ui:ui:$compose")
        implementation("androidx.compose.ui:ui-tooling-preview:$compose")
        implementation("androidx.compose.material3:material3:$material3composse")
        implementation("androidx.compose.material:material-icons-extended:$compose")

        implementation("androidx.lifecycle:lifecycle-runtime-ktx:$androidxLifecycle")
        implementation("androidx.lifecycle:lifecycle-runtime-compose:$androidxLifecycle")
        implementation("androidx.activity:activity-compose:$androidxActivityCompose")

        implementation("io.insert-koin:koin-android:$koin")
        implementation("io.insert-koin:koin-androidx-compose:$koin")

        implementation("com.google.accompanist:accompanist-pager:${accompoanist}")
        implementation("io.coil-kt:coil-compose:$coil")

        implementation("io.ktor:ktor-client-android:$ktor")
        implementation("io.ktor:ktor-client-logging:$ktor")
        implementation("io.ktor:ktor-client-content-negotiation:$ktor")
        implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationJson")

        implementation("com.jakewharton.timber:timber:$timber")

        debugImplementation("androidx.compose.ui:ui-tooling:$compose")
    }
}