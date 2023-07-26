package com.jonasgerdes.stoppelmap.preparation

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import org.koin.dsl.module
import java.io.File


val preparationModule = module {

    single {
        val appAssets = File("./android/app/src/main/assets/")
        val dataUpdateAssets = File("./android/feature-data-update/src/main/assets/")
        val resources = File("./preparation/src/main/resources")
        Settings(
            databaseFile = File(dataUpdateAssets, "shippedData.db"),
            geoJsonInput = File(resources, "stoma22.geojson"),
            geoJsonOutput = File("$dataUpdateAssets", "mapdata.geojson"),
            fetchedEventsFile = File(resources, "events/fetched.json"),
            manualEventsFile = File(resources, "events/manual.json"),
            descriptionFolder = File(resources, "descriptions")
        )
    }

    single<SqlDriver> {
        val settings: Settings = get()
        JdbcSqliteDriver("jdbc:sqlite:${settings.databaseFile.absoluteFile}")
    }
}
