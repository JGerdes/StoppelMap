plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    application
}

application {
    mainClass.set("com.jonasgerdes.stoppelmap.server.ApplicationKt")
}

java {
    targetCompatibility = JavaVersion.VERSION_17
}


ktor {
    fatJar {
        archiveFileName.set("stoppelmap-server.jar")
    }
}

dependencies {
    implementation(libs.bundles.ktor.server)
    implementation(libs.logback)
    implementation(libs.bundles.koin.server)
    implementation(libs.kotlinx.datetime)
    implementation(libs.jsoup)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.scrimage.core)
    implementation(libs.scrimage.webp)
    implementation(libs.blurhash.java)
}
