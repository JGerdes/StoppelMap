plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.sqldelight)
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
        create("Database") {
            packageName.set("com.jonasgerdes.stoppelmap.server")
            srcDirs.setFrom("src/main/sqldelight")
            schemaOutputDirectory =
                file("src/main/sqldelight/com/jonasgerdes/stoppelmap/server/schema")
            version = 1
            verifyMigrations = true
        }
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
    implementation(libs.sqldelight.driver.sqlite)
    implementation(libs.sqldelight.primitive.adapters)
}
