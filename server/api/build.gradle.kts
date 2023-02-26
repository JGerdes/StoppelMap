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
    with(DependencyVersions) {
        implementation("io.ktor:ktor-server-core:$ktorServer")
        implementation("io.ktor:ktor-server-netty:$ktorServer")
        implementation("io.ktor:ktor-server-call-logging:$ktorServer")
        implementation("ch.qos.logback:logback-classic:$logback")
    }
}
