import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin
    application
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

application {
    mainClass.set("com.jonasgerdes.stoppelmap.server.api.ApplicationKt")
}


java {
    targetCompatibility = JavaVersion.VERSION_17
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("stoppelmap-api")
    }
}

dependencies {
    implementation(libs.bundles.ktor.server)
    implementation(libs.logback)
}
