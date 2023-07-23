package com.jonasgerdes.stoppelmap

import android.content.Context
import com.jonasgerdes.stoppelmap.base.contract.AppInfo
import com.jonasgerdes.stoppelmap.base.contract.SeasonProvider
import com.jonasgerdes.stoppelmap.data.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.usecase.CopyDatabaseUseCase
import com.jonasgerdes.stoppelmap.util.StoppelmarktSeasonProvider
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val appModule = module {

    val databaseFileName = "stoma22.db"

    single<SqlDriver> {
        AndroidSqliteDriver(StoppelMapDatabase.Schema, context = get(), databaseFileName)
    }

    factory {
        CopyDatabaseUseCase(context = get(), databaseFileName = databaseFileName)
    }

    single<CoroutineScope> { CoroutineScope(Dispatchers.Main) }
    single<SeasonProvider> { StoppelmarktSeasonProvider(clockProvider = get()) }

    single {
        AppInfo(
            version = BuildConfig.VERSION_NAME,
            buildType = get<Context>().getString(R.string.build_type),
        )
    }
}
