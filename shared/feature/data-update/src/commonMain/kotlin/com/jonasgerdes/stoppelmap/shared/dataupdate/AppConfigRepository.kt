package com.jonasgerdes.stoppelmap.shared.dataupdate

import co.touchlab.kermit.Logger
import com.jonasgerdes.stoppelmap.shared.dataupdate.model.RemoteAppConfig
import com.jonasgerdes.stoppelmap.shared.dataupdate.model.Response
import com.jonasgerdes.stoppelmap.shared.dataupdate.model.VersionMessage
import com.jonasgerdes.stoppelmap.shared.dataupdate.source.remote.CdnSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import okio.Path

class AppConfigRepository(
    private val cdnSource: CdnSource,
    private val tempDatabase: Path,
    private val tempMapData: Path,
) {
    private val appConfigFlow = MutableStateFlow<RemoteAppConfig?>(null)
    val appConfig = appConfigFlow.asStateFlow()
    val messages: Flow<List<VersionMessage>> = appConfigFlow
        .filterNotNull()
        .map { it.messages }

    suspend fun updateAppConfig() {
        when (val response = cdnSource.getRemoteAppConfig()) {
            is Response.Error.HttpError ->
                Logger.e { "Fetching app config failed: ${response.status}." }

            is Response.Error.Other ->
                Logger.d { "Fetching app config failed: ${response.throwable?.stackTraceToString()}" }

            is Response.Success -> {
                val appConfig = response.body
                Logger.d { "Fetching app config succeeded: $appConfig" }
                appConfigFlow.value = appConfig
            }
        }
    }

    suspend fun downloadDatabase(databaseName: String): Path? {
        Logger.d { "Download dbm \"$databaseName\"" }
        return when (val response = cdnSource.downloadFile(tempDatabase, databaseName)) {
            is Response.Error.HttpError -> {
                Logger.e { "Downloading db \"$databaseName\" failed: ${response.status}." }
                null
            }

            is Response.Error.Other -> {
                Logger.e(response.throwable) { "Downloading db \"$databaseName\" failed" }
                null
            }

            is Response.Success -> {
                Logger.d { "Downloaded db succeeded." }
                response.body
            }
        }
    }

    suspend fun downloadMapDataFile(mapFileName: String): Path? {
        Logger.d { "Download geojson \"$mapFileName\"" }
        return when (val response = cdnSource.downloadFile(tempMapData, mapFileName)) {
            is Response.Error.HttpError -> {
                Logger.e { "Downloading geojson \"$mapFileName\" failed: ${response.status}." }
                null
            }

            is Response.Error.Other -> {
                Logger.e(response.throwable) { "Downloading geojson \"$mapFileName\" failed" }
                null
            }

            is Response.Success -> {
                Logger.d { "Downloaded geojson succeeded." }
                response.body
            }
        }
    }
}
