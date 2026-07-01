import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.skie)
}


kotlin {
    android {
        namespace = "com.jonasgerdes.stoppelmap.shared.home"
        compileSdk = libs.versions.compileSdk.get().toInt()
        minSdk = libs.versions.minSdk.get().toInt()
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
        androidResources { enable = true }
    }
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Home"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            // KMM
            implementation(libs.koin.core)
            implementation(libs.skie.annotations)
            api(libs.kmm.viewmodel)

            implementation(libs.kotlinx.datetime)
            implementation(libs.ktor.client.core)

            implementation(project(":shared:base"))
            implementation(project(":shared:resources"))
            implementation(project(":shared:dto"))
            implementation(project(":shared:data"))
            implementation(project(":shared:feature:countdown"))
            implementation(project(":shared:feature:data-update"))
            implementation(project(":shared:feature:schedule"))
        }
    }
}
