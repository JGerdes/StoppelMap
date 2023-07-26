package com.jonasgerdes.stoppelmap.dataupdate.usecase

import android.content.Context
import com.jonasgerdes.stoppelmap.base.contract.AppInfo
import com.jonasgerdes.stoppelmap.dataupdate.AppConfigRepository
import com.jonasgerdes.stoppelmap.dataupdate.VersioningRepository
import com.jonasgerdes.stoppelmap.dataupdate.util.removeDatabase
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import timber.log.Timber
import java.io.File

class UpdateAppConfigAndDownloadFilesUseCase(
    private val appConfigRepository: AppConfigRepository,
    private val appInfo: AppInfo,
    private val versioningRepository: VersioningRepository,
    private val context: Context,
    private val databaseFile: File
) {
    suspend operator fun invoke() {
        appConfigRepository.updateAppConfig()
        val appConfig = appConfigRepository.appConfig.first() ?: return
        val latestData = appConfig.data.latest
        if (
            appInfo.versionCode >= latestData.supportedSince.android
            && latestData.version > versioningRepository.getCurrentDatabaseVersion()
        ) {
            coroutineScope {
                val (dbFile, mapFile) = awaitAll(
                    async { appConfigRepository.downloadDatabase(latestData.data) },
                    async { appConfigRepository.downloadMapFile(latestData.map) },
                )
                Timber.d("Updated files. db: ${dbFile?.absoluteFile}, geojson: ${mapFile?.absoluteFile}")
                context.removeDatabase(databaseFile.nameWithoutExtension)
                dbFile?.copyTo(databaseFile, overwrite = true)
                versioningRepository.setDatabaseVersion(latestData.version)
            }
        }
    }
}
