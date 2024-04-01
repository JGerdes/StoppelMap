package com.jonasgerdes.stoppelmap.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import co.touchlab.kermit.Logger
import com.jonasgerdes.stoppelmap.CommonBuildConfig
import com.jonasgerdes.stoppelmap.base.contract.PreferencesPathFactory
import com.jonasgerdes.stoppelmap.base.model.AppInfo
import com.jonasgerdes.stoppelmap.base.model.DatabaseFile
import com.jonasgerdes.stoppelmap.base.model.MapDataFile
import com.jonasgerdes.stoppelmap.data.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.shared.dataupdate.io.toPath
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
private val preferencesPathFactory: PreferencesPathFactory = object : PreferencesPathFactory() {
    override fun createImpl(storageFile: String): String {
        val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
        return documentDirectory!!.path + "/$storageFile"
    }
}

@OptIn(ExperimentalForeignApi::class)
val appModule = module {

    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )!!

    single {
        DatabaseFile(documentDirectory.path!! + "/database.db")
    }

    single {
        MapDataFile(documentDirectory.path!! + "/mapdata.geojson")
    }

    single<PreferencesPathFactory> { preferencesPathFactory }

    single {
        AppInfo(
            versionName = "v0.0.1-${CommonBuildConfig.COMMIT_SHORT_SHA}-debug",
            versionCode = 1,
            commitSha = CommonBuildConfig.COMMIT_SHA,
            buildType = "debug",
            platform = "iOS"
        )
    }

    single<SqlDriver> {
        NativeSqliteDriver(
            schema = StoppelMapDatabase.Schema,
            name = get<DatabaseFile>().toPath().name.also { Logger.d { "database name: $it" } }
        )
    }
}
