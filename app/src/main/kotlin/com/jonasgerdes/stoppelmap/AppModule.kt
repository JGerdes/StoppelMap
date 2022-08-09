package com.jonasgerdes.stoppelmap

import com.jonasgerdes.stoppelmap.data.StoppelMapDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import org.koin.dsl.module

val appModule = module {
    single<SqlDriver> {
        AndroidSqliteDriver(StoppelMapDatabase.Schema, context = get(), "maenage.db")
    }

    single {
        StoppelMapDatabase(driver = get())
    }
}
