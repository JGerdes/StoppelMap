package com.jonasgerdes.stoppelmap.preparation

import com.jonasgerdes.stoppelmap.data.StoppelMapDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import org.koin.dsl.module
import java.io.File

private const val DB_FILE_PATH = "stoma22.db"
private val appAssets = File("../app/src/main/assets/")

val preparationModule = module {

    single {
        Settings(
            databaseFile = File(appAssets, DB_FILE_PATH)
        )
    }

    single<SqlDriver> {
        val settings: Settings = get()
        JdbcSqliteDriver("jdbc:sqlite:${settings.databaseFile.absoluteFile}")
    }

    single {
        StoppelMapDatabase(driver = get())
    }

}
