import Git.getCommit

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
            export(project(":shared:feature:home"))
            export(project(":shared:feature:licenses"))
            export(project(":shared:feature:data-update"))
            export(project(":shared:feature:countdown"))
            export(project(":shared:feature:schedule"))
            export(project(":shared:feature:transportation"))
            export(project(":shared:app"))
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(libs.moko.resources)
            api(project(":shared:base"))
            api(project(":shared:resources"))
            api(project(":shared:feature:home"))
            api(project(":shared:feature:licenses"))
            api(project(":shared:feature:data-update"))
            api(project(":shared:feature:countdown"))
            api(project(":shared:feature:schedule"))
            api(project(":shared:feature:transportation"))
            api(project(":shared:app"))
        }
    }
}

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

task("updateIosVersion") {
    // TODO: Improve this
    doLast {
        val projectFile = File(project.rootDir, "iosApp/iosApp.xcodeproj/project.pbxproj")
        val versionRegex = """MARKETING_VERSION = .*?;""".toRegex()
        val buildRegex = """CURRENT_PROJECT_VERSION = .\d*?;""".toRegex()
        val version = getVersion(getCommit().shortSha)
        val lines = projectFile.readLines()
            .map { original ->
                original
                    .replace(versionRegex, """MARKETING_VERSION = "${version.name}";""")
                    .replace(buildRegex, """CURRENT_PROJECT_VERSION = ${version.code};""")
                    .also {
                        if (original != it) {
                            println("Updated project.pbxproj: $it")
                        }
                    }
            }
        projectFile.writeText(
            lines.joinToString("\n")
        )
    }
}