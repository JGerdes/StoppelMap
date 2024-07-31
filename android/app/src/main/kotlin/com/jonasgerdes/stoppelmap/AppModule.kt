package com.jonasgerdes.stoppelmap

import android.content.Context
import android.content.Intent
import android.os.Build
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.jonasgerdes.stoppelmap.base.contract.PathFactory
import com.jonasgerdes.stoppelmap.base.contract.PreferencesPathFactory
import com.jonasgerdes.stoppelmap.base.model.AppInfo
import com.jonasgerdes.stoppelmap.base.model.DatabaseFile
import com.jonasgerdes.stoppelmap.base.model.MapDataFile
import com.jonasgerdes.stoppelmap.data.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.ui.StoppelMapActivity
import com.jonasgerdes.stoppelmap.widget.CreateStartAppIntentUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import java.io.File

private fun preferencesPathFactory(context: Context): PreferencesPathFactory =
    object : PreferencesPathFactory() {
        override fun createImpl(storageFile: String): String {
            return context.filesDir.resolve(storageFile).absolutePath
        }
    }

private fun pathFactory(context: Context): PathFactory =
    PathFactory { file -> context.filesDir.resolve(file).absolutePath }

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
            name = "stoppelMapData.db",
        )
    }

    single<CoroutineScope> { CoroutineScope(Dispatchers.Main) }

    single {
        AppInfo(
            versionName = BuildConfig.VERSION_NAME,
            versionCode = BuildConfig.VERSION_CODE,
            commitSha = CommonBuildConfig.COMMIT_SHA,
            buildType = get<Context>().getString(R.string.build_type),
            platform = "Android(${Build.VERSION.SDK_INT} on ${Build.MANUFACTURER}/${Build.MODEL})"
        )
    }

    single<PreferencesPathFactory> { preferencesPathFactory(get<Context>()) }
    single<PathFactory> { pathFactory(get()) }

    single<CreateStartAppIntentUseCase> {
        val context = get<Context>()
        CreateStartAppIntentUseCase { Intent(context, StoppelMapActivity::class.java) }
    }
}
