package com.jonasgerdes.stoppelmap.shared.dataupdate.usecase

import co.touchlab.kermit.Logger
import com.jonasgerdes.stoppelmap.base.model.AppInfo
import com.jonasgerdes.stoppelmap.shared.dataupdate.AppConfigRepository
import com.jonasgerdes.stoppelmap.shared.dataupdate.VersioningRepository
import com.jonasgerdes.stoppelmap.shared.dataupdate.model.Data
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import okio.FileSystem
import okio.Path
import okio.SYSTEM

class UpdateAppConfigAndDownloadFilesUseCase(
    private val appInfo: AppInfo,
    private val appConfigRepository: AppConfigRepository,
    private val versioningRepository: VersioningRepository,
    private val databaseFile: Path,
    private val mapDataFile: Path,
) {
    suspend operator fun invoke() {
        appConfigRepository.updateAppConfig()
        val appConfig = appConfigRepository.appConfig.first() ?: return
        val latestData = appConfig.data.latest
        if (
            appInfo.versionCode >= latestData.supportedSince.onCurrentPlatform()
            && latestData.version > versioningRepository.getCurrentDatabaseVersion()
        ) {
            Logger.d { "There are new files available online. brb, downloading them." }
            coroutineScope {
                val (downloadedDatabaseFile, downloadedMapDataFile) = awaitAll(
                    async { appConfigRepository.downloadDatabase(latestData.data) },
                    async { appConfigRepository.downloadMapDataFile(latestData.map) },
                )
                Logger.d { "Downloaded files db: $downloadedDatabaseFile, geojson: $downloadedMapDataFile" }
                Logger.d { "Copy successful ones to destination" }

                if (downloadedDatabaseFile != null && downloadedMapDataFile != null) {
                    FileSystem.SYSTEM.copy(downloadedDatabaseFile, databaseFile)
                    versioningRepository.setDatabaseVersion(latestData.version)

                    FileSystem.SYSTEM.copy(downloadedMapDataFile, mapDataFile)
                    versioningRepository.setMapDataVersion(latestData.version)
                }
                Logger.d { "Copying of new files succeeded" }

            }
        }
    }
}

expect fun Data.SupportedSince.onCurrentPlatform(): Int
