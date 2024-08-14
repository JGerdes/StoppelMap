package com.jonasgerdes.stoppelmap.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import co.touchlab.sqliter.DatabaseConfiguration
import com.jonasgerdes.stoppelmap.CommonBuildConfig
import com.jonasgerdes.stoppelmap.base.contract.PathFactory
import com.jonasgerdes.stoppelmap.base.contract.PreferencesPathFactory
import com.jonasgerdes.stoppelmap.base.model.AppInfo
import com.jonasgerdes.stoppelmap.base.model.MapDataFile
import com.jonasgerdes.stoppelmap.data.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.dto.data.StoppelMapData
import com.jonasgerdes.stoppelmap.map.location.IosLocationRepository
import com.jonasgerdes.stoppelmap.map.location.IosPermissionRepository
import com.jonasgerdes.stoppelmap.map.location.LocationAndPermissionDependencies.locationAndPermissionService
import com.jonasgerdes.stoppelmap.map.location.LocationRepository
import com.jonasgerdes.stoppelmap.map.location.PermissionRepository
import kotlinx.cinterop.ExperimentalForeignApi
import okio.Path.Companion.toPath
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.UIKit.UIDevice

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
private val pathFactory: PathFactory = PathFactory { file ->
    val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    documentDirectory!!.path + "/$file"
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
        MapDataFile(documentDirectory.path!! + "/mapdata.geojson")
    }

    single<PreferencesPathFactory> { preferencesPathFactory }
    single<PathFactory> { pathFactory }

    single {
        AppInfo(
            versionName = CommonBuildConfig.VERSION_NAME,
            versionCode = CommonBuildConfig.VERSION_CODE,
            commitSha = CommonBuildConfig.COMMIT_SHA,
            buildType = "debug",
            os = "iOS ${UIDevice.currentDevice().systemVersion}",
            device = "Apple ${UIDevice.currentDevice().name}",
            platform = "iOS(${UIDevice.currentDevice().systemVersion} on Apple/${UIDevice.currentDevice().name})",
        )
    }

    single<SqlDriver> {
        val databaseDir = documentDirectory.path!!
        val databaseFile = "$databaseDir/stoppelMapData_v${StoppelMapData.schemaVersion}.db".toPath()
        val databaseName = databaseFile.name
        NativeSqliteDriver(
            schema = StoppelMapDatabase.Schema,
            name = databaseName,
            onConfiguration = {
                it.copy(
                    extendedConfig = DatabaseConfiguration.Extended(
                        basePath = databaseDir
                    )
                )
            }
        )
    }

    single<LocationRepository> { IosLocationRepository(locationAndPermissionService = locationAndPermissionService) }
    single<PermissionRepository> { IosPermissionRepository(locationAndPermissionService = locationAndPermissionService) }
}
