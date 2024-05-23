package com.jonasgerdes.stoppelmap.preparation

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import org.koin.dsl.module
import java.io.File


val preparationModule = module {

    single {
        val sharedAssets = File("../shared/resources/src/commonMain/resources/MR/assets/")
        val resources = File("../preparation/src/main/resources")
        Settings(
            databaseFile = File(sharedAssets, "database.db"),
            geoJsonInput = File(resources, "stoma23.geojson"),
            geoJsonOutput = File(sharedAssets, "mapdata.geojson"),
            fetchedEventsFile = File(resources, "events/fetched.json"),
            manualEventsFile = File(resources, "events/manual.json"),
            descriptionFolder = File(resources, "descriptions"),
            year = 2023
        )
    }

    single<SqlDriver> {
        val settings: Settings = get()
        JdbcSqliteDriver("jdbc:sqlite:${settings.databaseFile.absoluteFile}")
    }
}
