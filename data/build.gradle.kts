plugins {
    id("kotlin")
    alias(libs.plugins.sqldelight)
}

sqldelight {
    database("StoppelMapDatabase") {
        packageName = "com.jonasgerdes.stoppelmap.data"
        sourceFolders = listOf("sqldelight")
        schemaOutputDirectory = file("src/main/sqldelight/com/jonasgerdes/stoppelmap/data/schema")
        version = 1
        verifyMigrations = true
    }
}

dependencies {
    implementation(libs.kotlinx.datetime)
    implementation(libs.koin.core)
}
