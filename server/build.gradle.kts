plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.kotlin.serialization)
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

sqldelight {
    databases {
        create("NewsDatabase") {
            packageName.set("com.jonasgerdes.stoppelmap.server.news.data")
            srcDirs.setFrom("src/main/sqldelight")
            schemaOutputDirectory =
                file("src/main/sqldelight/com/jonasgerdes/stoppelmap/server/news/data/schema")
            version = 1
            verifyMigrations = true
        }
    }
}

dependencies {
    implementation(project(":shared:dto"))
    implementation(libs.bundles.ktor.server)
    implementation(libs.logback)
    implementation(libs.bundles.koin.server)
    implementation(libs.kotlinx.datetime)
    implementation(libs.jsoup)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.server.metrics.micrometer)
    implementation(libs.micrometer.registry.prometheus)
    implementation(libs.scrimage.core)
    implementation(libs.scrimage.webp)
    implementation(libs.blurhash.java)
    implementation(libs.sqldelight.driver.sqlite)
    implementation(libs.sqldelight.primitive.adapters)
}
