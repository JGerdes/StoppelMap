package com.jonasgerdes.stoppelmap.dataupdate.usecase

import com.jonasgerdes.stoppelmap.base.contract.AppInfo
import com.jonasgerdes.stoppelmap.dataupdate.AppConfigRepository
import com.jonasgerdes.stoppelmap.dataupdate.VersioningRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import timber.log.Timber
import java.io.File

class UpdateAppConfigAndDownloadFilesUseCase(
    private val appInfo: AppInfo,
    private val appConfigRepository: AppConfigRepository,
    private val versioningRepository: VersioningRepository,
    private val databaseFile: File,
    private val mapDataFile: File,
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
                val (downloadedDatabaseFile, downloadedMapDataFile) = awaitAll(
                    async { appConfigRepository.downloadDatabase(latestData.data) },
                    async { appConfigRepository.downloadMapDataFile(latestData.map) },
                )
                Timber.d("Updated files. db: ${downloadedDatabaseFile?.absoluteFile}, geojson: ${downloadedMapDataFile?.absoluteFile}")

                if (downloadedDatabaseFile != null && downloadedMapDataFile != null) {
                    downloadedDatabaseFile.copyTo(databaseFile, overwrite = true)
                    versioningRepository.setDatabaseVersion(latestData.version)

                    downloadedMapDataFile.copyTo(mapDataFile, overwrite = true)
                    versioningRepository.setMapDataVersion(latestData.version)
                }


            }
        }
    }
}
