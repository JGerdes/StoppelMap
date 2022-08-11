package com.jonasgerdes.stoppelmap.preparation

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import org.koin.dsl.module
import java.io.File


val preparationModule = module {

    single {
        val appAssets = File("../app/src/main/assets/")
        val mapFeatureAssets = File("../feature-map/src/main/assets/")
        val resources = File("../preparation/src/main/resources")
        Settings(
            databaseFile = File(appAssets, "stoma22.db"),
            geoJsonInput = File(resources, "stoma22.geojson"),
            geoJsonOutput = File("$mapFeatureAssets/map", "stoma22.geojson"),
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
