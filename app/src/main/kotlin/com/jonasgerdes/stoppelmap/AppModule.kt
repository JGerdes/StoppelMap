package com.jonasgerdes.stoppelmap

import com.jonasgerdes.stoppelmap.base.contract.ClockProvider
import com.jonasgerdes.stoppelmap.base.contract.SeasonProvider
import com.jonasgerdes.stoppelmap.data.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.usecase.CopyDatabaseUseCase
import com.jonasgerdes.stoppelmap.util.StoppelmarktClockProvider
import com.jonasgerdes.stoppelmap.util.StoppelmarktSeasonProvider
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import org.koin.dsl.module

val appModule = module {

    val databaseFileName = "stoma22.db"

    single<SqlDriver> {
        AndroidSqliteDriver(StoppelMapDatabase.Schema, context = get(), databaseFileName)
    }

    factory {
        CopyDatabaseUseCase(context = get(), databaseFileName = databaseFileName)
    }

    single<ClockProvider> { StoppelmarktClockProvider() }
    single<SeasonProvider> { StoppelmarktSeasonProvider(clockProvider = get()) }
}
