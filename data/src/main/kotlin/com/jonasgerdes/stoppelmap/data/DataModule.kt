package com.jonasgerdes.stoppelmap.data

import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import org.koin.dsl.module

val dataModule = module {

    single<SqlDriver> {
        AndroidSqliteDriver(StoppelMapDatabase.Schema, context = get(), "stoma22.db")
    }

    single {
        StoppelMapDatabase(driver = get())
    }
}
