plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
    }
}

android {
    namespace = "com.jonasgerdes.stoppelmap.map"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = ProjectDefaults.MINIFY_RELEASE
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":shared:base"))
    implementation(project(":shared:resources"))
    implementation(project(":base"))
    implementation(project(":shared:data"))
    implementation(project(":shared:feature:map"))
    // TODO: Resolve this in a better way
    implementation(project(":shared:feature:schedule"))
    implementation(project(":android:theme"))
    implementation(project(":android:feature-settings"))

    implementation(libs.bundles.compose.ui)
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.datetime)
    implementation(libs.bundles.androidx.runtime)
    implementation(libs.androidx.activity.compose)
    implementation(libs.bundles.koin.compose)
    implementation(libs.timber)
    implementation(libs.maplibre)
    implementation(libs.accompanist.permissions)
    implementation(libs.google.play.services.location)
    implementation(libs.sqldelight.coroutines)
    implementation(libs.androidx.datastore)
    implementation(libs.kotlinx.serialization.json.core)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.coil.compose)

}
