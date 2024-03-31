package com.jonasgerdes.stoppelmap

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.jonasgerdes.stoppelmap.base.model.AppInfo
import com.jonasgerdes.stoppelmap.base.model.DatabaseFile
import com.jonasgerdes.stoppelmap.base.model.MapDataFile
import com.jonasgerdes.stoppelmap.data.StoppelMapDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import java.io.File

val appModule = module {

    single {
        DatabaseFile(
            file = File(
                File(get<Context>().filesDir.parentFile, "databases"),
                "database.db"
            )
        )
    }

    single {
        MapDataFile(
            file = File(get<Context>().filesDir, "mapdata.geojson")
        )
    }

    single<SqlDriver> {
        AndroidSqliteDriver(
            schema = StoppelMapDatabase.Schema,
            context = get(),
            name = get<DatabaseFile>().file.name,
        )
    }

    single<CoroutineScope> { CoroutineScope(Dispatchers.Main) }

    single {
        AppInfo(
            versionName = BuildConfig.VERSION_NAME,
            versionCode = BuildConfig.VERSION_CODE,
            commitSha = CommonBuildConfig.COMMIT_SHA,
            buildType = get<Context>().getString(R.string.build_type),
            platform = "Android"
        )
    }
}
