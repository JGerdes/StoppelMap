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
    with(DependencyVersions) {
        implementation("androidx.compose.ui:ui:$compose")
        implementation("androidx.compose.material3:material3:$material3compose")
        implementation("androidx.compose.material:material-icons-extended:$compose")
        implementation("androidx.compose.ui:ui-tooling-preview:$compose")

        debugImplementation("androidx.compose.ui:ui-tooling:$compose")

    }
}
