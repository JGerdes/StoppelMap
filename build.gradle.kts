buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21")
        with(DependencyVersions) {
            classpath("com.android.tools.build:gradle:$gradlePlugin")
            classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin")
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
