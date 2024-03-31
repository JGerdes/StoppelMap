package com.jonasgerdes.stoppelmap.di

import com.jonasgerdes.stoppelmap.CommonBuildConfig
import com.jonasgerdes.stoppelmap.base.model.AppInfo
import com.jonasgerdes.stoppelmap.base.model.DatabaseFile
import com.jonasgerdes.stoppelmap.base.model.MapDataFile
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

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


    single {
        AppInfo(
            versionName = "v0.0.1-${CommonBuildConfig.COMMIT_SHORT_SHA}-debug",
            versionCode = 1,
            commitSha = CommonBuildConfig.COMMIT_SHA,
            buildType = "debug",
            platform = "iOS"
        )
    }
}
