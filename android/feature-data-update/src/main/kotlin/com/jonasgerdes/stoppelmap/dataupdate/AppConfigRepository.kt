package com.jonasgerdes.stoppelmap.dataupdate

import com.jonasgerdes.stoppelmap.dataupdate.model.RemoteAppConfig
import com.jonasgerdes.stoppelmap.dataupdate.model.Response
import com.jonasgerdes.stoppelmap.dataupdate.model.VersionMessage
import com.jonasgerdes.stoppelmap.dataupdate.source.remote.CdnSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.File

class AppConfigRepository(
    private val cdnSource: CdnSource,
    private val cacheDir: File,
) {
    private val appConfigFlow = MutableStateFlow<RemoteAppConfig?>(null)
    val appConfig = appConfigFlow.asStateFlow()
    val messages: Flow<List<VersionMessage>> = appConfigFlow
        .filterNotNull()
        .map { it.messages }

    suspend fun updateAppConfig() {
        when (val response = cdnSource.getRemoteAppConfig()) {
            is Response.Error.HttpError ->
                Timber.e("Fetching app config failed: ${response.status}.")

            is Response.Error.Other ->
                Timber.d("Fetching app config failed: ${response.throwable?.stackTraceToString()}")

            is Response.Success -> {
                val appConfig = response.body
                Timber.d("Fetching app config succeeded: $appConfig")
                appConfigFlow.value = appConfig
            }
        }
    }

    suspend fun downloadDatabase(databaseName: String): File? {
        Timber.d("Download dbm \"$databaseName\"")
        return when (val response = cdnSource.downloadFile(cacheDir, databaseName)) {
            is Response.Error.HttpError -> {
                Timber.e("Downloading db \"$databaseName\" failed: ${response.status}.")
                null
            }

            is Response.Error.Other -> {
                Timber.e("Downloading db \"$databaseName\" failed: ${response.throwable?.stackTraceToString()}")
                null
            }

            is Response.Success -> {
                Timber.d("Downloaded db succeeded.")
                response.body
            }
        }
    }

    suspend fun downloadMapFile(mapFileName: String): File? {
        Timber.d("Download geojson \"$mapFileName\"")
        return when (val response = cdnSource.downloadFile(cacheDir, mapFileName)) {
            is Response.Error.HttpError -> {
                Timber.e("Downloading geojson \"$mapFileName\" failed: ${response.status}.")
                null
            }

            is Response.Error.Other -> {
                Timber.e("Downloading geojson \"$mapFileName\" failed: ${response.throwable?.stackTraceToString()}")
                null
            }

            is Response.Success -> {
                Timber.d("Downloaded geojson succeeded.")
                response.body
            }
        }
    }
}
