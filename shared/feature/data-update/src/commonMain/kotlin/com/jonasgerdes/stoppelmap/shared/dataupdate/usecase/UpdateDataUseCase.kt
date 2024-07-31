package com.jonasgerdes.stoppelmap.shared.dataupdate.usecase

import co.touchlab.kermit.Logger
import com.jonasgerdes.stoppelmap.base.model.AppInfo
import com.jonasgerdes.stoppelmap.data.conversion.usecase.UpdateDatabaseUseCase
import com.jonasgerdes.stoppelmap.dto.config.Data
import com.jonasgerdes.stoppelmap.dto.data.StoppelMapData
import com.jonasgerdes.stoppelmap.shared.dataupdate.data.onCurrentPlatform
import com.jonasgerdes.stoppelmap.shared.dataupdate.repository.AppConfigRepository
import com.jonasgerdes.stoppelmap.shared.dataupdate.repository.AppConfigRepository.AppConfigState
import com.jonasgerdes.stoppelmap.shared.dataupdate.repository.DataUpdateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.okio.decodeFromBufferedSource
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import okio.Sink
import okio.Source
import okio.buffer
import okio.openZip
import okio.use

@OptIn(ExperimentalSerializationApi::class)
class UpdateDataUseCase(
    private val appInfo: AppInfo,
    private val bundledDataPath: Path,
    private val bundledDataFileSystem: FileSystem,
    private val persistentDataDirectory: Path,
    private val dataUpdateRepository: DataUpdateRepository,
    private val appConfigRepository: AppConfigRepository,
    private val updateDatabase: UpdateDatabaseUseCase,
    private val persistentFileSystem: FileSystem,
) {

    suspend operator fun invoke() {
        var currentDataVersion = dataUpdateRepository.currentDataVersion.first()
        if (currentDataVersion == null || appInfo.versionCode > currentDataVersion) {
            Logger.d { "UpdateData: Bundled data is newer then existing (or none exists). Apply data from bundle" }
            updateDataFromZipFile(bundledDataFileSystem, bundledDataPath)
        } else {
            Logger.d { "UpdateData: Existing data is newer (or same version) as bundled (bundled data: ${appInfo.versionCode}, existing data: $currentDataVersion" }
        }
        currentDataVersion = dataUpdateRepository.currentDataVersion.filterNotNull().first()
        val latestRemoteData = appConfigRepository.appConfig
            .filterNot { it is AppConfigState.Pending }
            .map {
                when (it) {
                    is AppConfigState.Available -> it.appConfig.data.latest
                    AppConfigState.FailedToFetch -> null
                    AppConfigState.Pending -> null
                }
            }.first()

        if (latestRemoteData != null && latestRemoteData.version > currentDataVersion
            && appInfo.versionCode > latestRemoteData.supportedSince.onCurrentPlatform()
        ) {
            Logger.d { "UpdateData: Remote data available that is newer then existing. Downloading it" }
            updateDataFromRemote(latestRemoteData)
        } else {
            Logger.d { "UpdateData: Existing data is newer (or same version) as remote data (remote data: ${latestRemoteData?.version}, existing data: $currentDataVersion" }
        }
        Logger.d { "UpdateData: Done updating data" }
    }


    private suspend fun updateDataFromRemote(latestRemoteData: Data) = withContext(Dispatchers.IO) {
        val remoteDataZip = dataUpdateRepository.downloadFile(latestRemoteData.file, persistentFileSystem)
        if (remoteDataZip != null) {
            Logger.d { "Downloaded remote data sucessfully, appling it" }
            updateDataFromZipFile(persistentFileSystem, remoteDataZip)
        } else {
            Logger.d { "Failed to download remote data" }
        }
    }

    private suspend fun updateDataFromZipFile(fileSystem: FileSystem, zipPath: Path) = withContext(Dispatchers.IO) {
        val bundledData = fileSystem.openZip(zipPath)
        Logger.d { "Copy map data" }
        persistentFileSystem.createDirectories(persistentDataDirectory)
        val mapDataDestination = persistentDataDirectory.resolve("mapdata_${appInfo.versionCode}.geojson")
        bundledData.source("mapdata.geojson".toPath()).copyTo(persistentFileSystem.sink(mapDataDestination))
        Logger.d { "Read stoppelMapData.json" }
        val stoppelMapData = bundledData.source("stoppelMapData.json".toPath())
            .buffer()
            .use {
                Json.decodeFromBufferedSource<StoppelMapData>(it)
            }
        Logger.d { "Update database" }
        updateDatabase(stoppelMapData)
        Logger.d { "Set new map file $mapDataDestination" }
        dataUpdateRepository.setMapFile(mapDataDestination)
    }


    private fun Source.copyTo(sink: Sink) =
        buffer().use { source ->
            sink.buffer().use {
                it.writeAll(source)
            }
        }
}