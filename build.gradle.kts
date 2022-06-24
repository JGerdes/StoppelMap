buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies { with(DependencyVersions) {
            classpath("com.android.tools.build:gradle:$gradlePlugin")
            classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin")
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
