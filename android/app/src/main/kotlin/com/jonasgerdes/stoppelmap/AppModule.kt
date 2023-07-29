package com.jonasgerdes.stoppelmap

import android.content.Context
import com.jonasgerdes.stoppelmap.base.contract.AppInfo
import com.jonasgerdes.stoppelmap.base.contract.DatabaseFile
import com.jonasgerdes.stoppelmap.base.contract.MapDataFile
import com.jonasgerdes.stoppelmap.base.contract.SeasonProvider
import com.jonasgerdes.stoppelmap.base.contract.Secrets
import com.jonasgerdes.stoppelmap.data.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.util.StoppelmarktSeasonProvider
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import java.io.File

val appModule = module {

    single {
        DatabaseFile(
            databaseFile = File(
                File(get<Context>().filesDir.parentFile, "databases"),
                "database.db"
            )
        )
    }

    single {
        MapDataFile(
            mapDataFile = File(get<Context>().filesDir, "mapdata.geojson")
        )
    }

    single<SqlDriver> {
        AndroidSqliteDriver(
            schema = StoppelMapDatabase.Schema,
            context = get(),
            name = get<DatabaseFile>().databaseFile.name,
        )
    }

    single<CoroutineScope> { CoroutineScope(Dispatchers.Main) }
    single<SeasonProvider> { StoppelmarktSeasonProvider(clockProvider = get()) }

    single {
        AppInfo(
            versionName = BuildConfig.VERSION_NAME,
            versionCode = BuildConfig.VERSION_CODE,
            commitSha = BuildConfig.COMMIT_SHA,
            buildType = get<Context>().getString(R.string.build_type),
            platform = "Android"
        )
    }

    single {
        Secrets(
            stoppelMapApiKey = BuildConfig.STOPPELMAP_API_KEY
        )
    }
}
