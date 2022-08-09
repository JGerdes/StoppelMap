buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        with(DependencyVersions) {
            classpath("com.android.tools.build:gradle:$gradlePlugin")
            classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin")
            classpath("com.squareup.sqldelight:gradle-plugin:$sqldelight")
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
