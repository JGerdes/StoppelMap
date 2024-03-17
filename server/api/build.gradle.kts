plugins {
    alias(libs.plugins.kotlin.jvm)
    application
}

application {
    mainClass.set("com.jonasgerdes.stoppelmap.server.api.ApplicationKt")
}


java {
    targetCompatibility = JavaVersion.VERSION_17
}


ktor {
    fatJar {
        archiveFileName.set("stoppelmap-api.jar")
    }
}

dependencies {
    implementation(libs.bundles.ktor.server)
    implementation(libs.logback)
}
