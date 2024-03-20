plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.skie)
    alias(libs.plugins.moko.resources)
}


kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = ProjectDefaults.KOTLIN_JVM_TARGET
            }
        }
    }
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
            export(libs.moko.resources)
            export(project(":shared:base"))
            export(project(":shared:resources"))
            export(project(":shared:feature:countdown"))
            export(project(":shared:app"))
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(libs.moko.resources)
            api(project(":shared:base"))
            api(project(":shared:resources"))
            api(project(":shared:feature:countdown"))
            api(project(":shared:app"))
        }
    }
}

/*multiplatformResources {
    multiplatformResourcesPackage = "com.jonasgerdes.stoppelmap.shared"
    iosBaseLocalizationRegion = "de"
    multiplatformResourcesClassName = "MRShared"
}*/

android {
    namespace = "com.jonasgerdes.stoppelmap.shared"
    compileSdk = libs.versions.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = ProjectDefaults.JAVA_COMPATIBILITY_VERSION
        targetCompatibility = ProjectDefaults.JAVA_COMPATIBILITY_VERSION
    }
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }
}
