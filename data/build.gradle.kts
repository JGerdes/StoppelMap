plugins {
    id("kotlin")
    id("com.squareup.sqldelight")
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

    with(DependencyVersions) {
        implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinxDatetime")
        implementation("io.insert-koin:koin-core:$koin")
    }
}
