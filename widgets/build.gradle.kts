import DependencyVersions.androidxActivityCompose
import DependencyVersions.androidxCore
import DependencyVersions.androidxLifecycle
import DependencyVersions.androidxNavigation
import DependencyVersions.compose
import DependencyVersions.desugarSDK
import DependencyVersions.koin
import DependencyVersions.kotlinxDatetime
import DependencyVersions.material3
import DependencyVersions.timber

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.jonasgerdes.stoppelmap.widgets"
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
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:$androidxCore")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinxDatetime")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:$desugarSDK")

    implementation("androidx.compose.ui:ui:$compose")
    implementation("androidx.compose.ui:ui-tooling-preview:$compose")
    implementation("androidx.compose.material3:material3:$material3")
    implementation("androidx.compose.material:material-icons-extended:$compose")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$androidxLifecycle")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:$androidxLifecycle")
    implementation("androidx.activity:activity-compose:$androidxActivityCompose")
    implementation("androidx.navigation:navigation-compose:$androidxNavigation")

    implementation("io.insert-koin:koin-android:$koin")
    implementation("io.insert-koin:koin-androidx-compose:$koin")

    implementation("com.jakewharton.timber:timber:$timber")


    debugImplementation("androidx.compose.ui:ui-tooling:$compose")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}
