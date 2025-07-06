plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.moko.resources)
    alias(libs.plugins.skie)
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
            baseName = "Resources"
            isStatic = true
        }
    }

    sourceSets {
        getByName("androidMain").dependsOn(commonMain.get())
        getByName("iosArm64Main").dependsOn(commonMain.get())
        getByName("iosX64Main").dependsOn(commonMain.get())
        getByName("iosSimulatorArm64Main").dependsOn(commonMain.get())
        val iosMain by creating {
            dependsOn(commonMain.get())
            getByName("iosArm64Main").dependsOn(this)
            getByName("iosX64Main").dependsOn(this)
            getByName("iosSimulatorArm64Main").dependsOn(this)
        }

        commonMain.dependencies {
            implementation(libs.skie.annotations)
            implementation(libs.kotlinx.datetime)
            api(libs.moko.resources)
        }
    }
}

multiplatformResources {
    resourcesPackage.set("com.jonasgerdes.stoppelmap.shared.resources")
    iosBaseLocalizationRegion.set("de")
    resourcesClassName.set("Res")
}

android {
    namespace = "com.jonasgerdes.stoppelmap.shared.resources"
    compileSdk = libs.versions.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = ProjectDefaults.JAVA_COMPATIBILITY_VERSION
        targetCompatibility = ProjectDefaults.JAVA_COMPATIBILITY_VERSION
    }
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }
}
