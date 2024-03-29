package com.jonasgerdes.stoppelmap.dataupdate.usecase

import android.content.Context
import com.jonasgerdes.stoppelmap.base.contract.AppInfo
import com.jonasgerdes.stoppelmap.dataupdate.VersioningRepository
import com.jonasgerdes.stoppelmap.dataupdate.util.copyToFile
import com.jonasgerdes.stoppelmap.dataupdate.util.removeDatabase
import java.io.File

class CopyAssetDataFilesUseCase(
    private val appInfo: AppInfo,
    private val versioningRepository: VersioningRepository,
    private val context: Context,
    private val databaseFile: File,
    private val mapDataFile: File,
) {
    suspend operator fun invoke() {
        if (appInfo.versionCode > versioningRepository.getCurrentDatabaseVersion()) {
            context.removeDatabase(databaseFile.nameWithoutExtension)
            context.assets.copyToFile(databaseFile.name, databaseFile)
            versioningRepository.setDatabaseVersion(appInfo.versionCode)
        }
        if (appInfo.versionCode > versioningRepository.getCurrentMapDataVersion()) {
            context.assets.copyToFile(mapDataFile.name, mapDataFile)
            versioningRepository.setMapDataVersion(appInfo.versionCode)
        }
    }
}
