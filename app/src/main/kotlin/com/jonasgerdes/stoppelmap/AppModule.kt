package com.jonasgerdes.stoppelmap

import com.jonasgerdes.stoppelmap.data.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.usecase.CopyDatabaseUseCase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import kotlinx.datetime.*
import org.koin.dsl.module

val appModule = module {

    val stoppelmarktTimeZone = TimeZone.of("Europe/Berlin")

    val databaseFileName = "stoma22.db"

    single<SqlDriver> {
        AndroidSqliteDriver(StoppelMapDatabase.Schema, context = get(), databaseFileName)
    }

    single {
        {
            Clock.System.now().toLocalDateTime(stoppelmarktTimeZone).let {
                LocalDateTime(2022, Month.AUGUST, 16, 19, 0)
            }
        }
    }

    factory {
        CopyDatabaseUseCase(context = get(), databaseFileName = databaseFileName)
    }
}
