package com.jonasgerdes.stoppelmap.shared.dataupdate.usecase

import co.touchlab.kermit.Logger
import com.jonasgerdes.stoppelmap.base.model.AppInfo
import com.jonasgerdes.stoppelmap.shared.dataupdate.VersioningRepository
import dev.icerock.moko.resources.AssetResource
import okio.Path

class CopyAssetDataFilesUseCase(
    private val appInfo: AppInfo,
    private val versioningRepository: VersioningRepository,
    private val removeDatabaseFile: RemoveDatabaseFileUseCase,
    private val copyAssetToFile: CopyAssetToFileUseCase,
    private val databaseFile: Path,
    private val mapDataFile: Path,
) {
    suspend operator fun invoke(
        databaseAsset: AssetResource,
        mapdataAsset: AssetResource,
    ) {
        Logger.d {
            """Copy assets bundled with app (${appInfo.versionCode}) if needed:
            |   Database: ${versioningRepository.getCurrentDatabaseVersion()}
            |   MapData: ${versioningRepository.getCurrentMapDataVersion()}
        """.trimMargin()
        }
        if (appInfo.versionCode > versioningRepository.getCurrentDatabaseVersion()) {
            removeDatabaseFile(databaseFile)
            copyAssetToFile(databaseAsset, databaseFile)
            versioningRepository.setDatabaseVersion(appInfo.versionCode)
        }
        if (appInfo.versionCode > versioningRepository.getCurrentMapDataVersion()) {
            copyAssetToFile(mapdataAsset, mapDataFile)
            versioningRepository.setMapDataVersion(appInfo.versionCode)
        }
    }
}
