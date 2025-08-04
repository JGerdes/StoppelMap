package com.jonasgerdes.stoppelmap.preparation

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.jonasgerdes.stoppelmap.data.conversion.usecase.UpdateDatabaseUseCase
import org.koin.dsl.module
import java.io.File


val preparationModule = module {

    single {
        val sharedAssets = File("../shared/resources/src/commonMain/moko-resources/assets/")
        val resources = File("../preparation/src/main/resources")
        val staticServerDir = System.getenv("SERVER_STATIC_DIR")?.let { File(it) }
        Settings(
            databaseFile = File("database.db"),
            geoJsonInput = File(resources, "stoma24.geojson"),
            dataOutputDir = sharedAssets,
            staticServerDir = staticServerDir,
            tempDir = File("temp").also { it.mkdirs() },
            fetchedEventsFile = File(resources, "events/fetched.json"),
            eventLocationsFile = File(resources, "events/locations.json"),
            manualEventsFile = File(resources, "events/manual.json"),
            crawledRoutesDirectory = File(resources, "routes").also { if (it.exists().not()) it.mkdirs() },
            descriptionFolder = File(resources, "descriptions"),
            year = 2024
        )
    }

    single<SqlDriver> {
        val settings: Settings = get()
        JdbcSqliteDriver("jdbc:sqlite:${settings.databaseFile.absoluteFile}")
    }

    factory {
        UpdateDatabaseUseCase(
            stoppelMapDatabase = get()
        )
    }
}
