package com.jonasgerdes.stoppelmap

import com.jonasgerdes.stoppelmap.data.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.usecase.CopyDatabaseUseCase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import org.koin.dsl.module

val appModule = module {

    val databaseFileName = "stoma22.db"

    single<SqlDriver> {
        AndroidSqliteDriver(StoppelMapDatabase.Schema, context = get(), databaseFileName)
    }

    single {
        StoppelMapDatabase(driver = get())
    }

    factory {
        CopyDatabaseUseCase(context = get(), databaseFileName = databaseFileName)
    }
}
