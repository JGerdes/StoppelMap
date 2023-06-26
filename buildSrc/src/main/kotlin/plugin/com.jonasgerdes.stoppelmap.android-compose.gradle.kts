plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = DependencyVersions.compose
    }
}

dependencies {
    implementation(libs.compose.ui.core)
    implementation(libs.compose.ui.preview)
    implementation(libs.compose.material.icons)
    implementation(libs.compose.material.components)

    debugImplementation(libs.compose.ui.tooling)
}
