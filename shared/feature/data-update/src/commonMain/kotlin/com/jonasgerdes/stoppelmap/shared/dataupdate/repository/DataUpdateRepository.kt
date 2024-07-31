package com.jonasgerdes.stoppelmap.shared.dataupdate.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import co.touchlab.kermit.Logger
import com.jonasgerdes.stoppelmap.data.MetadataQueries
import com.jonasgerdes.stoppelmap.shared.dataupdate.source.remote.RemoteStaticFileSource
import com.jonasgerdes.stoppelmap.shared.network.model.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath


class DataUpdateRepository(
    private val dataStore: DataStore<Preferences>,
    private val metadataQueries: MetadataQueries,
    private val remoteStaticFileSource: RemoteStaticFileSource,
    private val tempFileDirectory: Path,
) {

    private val mapFileKey = stringPreferencesKey("mapFile")

    val currentDataVersion: Flow<Long?>
        get() =
            metadataQueries.get().asFlow()
                .mapToOneOrNull(Dispatchers.IO)
                .map { it?.version }

    val mapFile: Flow<Path?> = dataStore.data.map { it[mapFileKey]?.toPath() }

    suspend fun setMapFile(mapFilePath: Path) {
        dataStore.edit { it[mapFileKey] = mapFilePath.toString() }
    }

    suspend fun downloadFile(fileName: String, toFileSystem: FileSystem): Path? {
        val destination = tempFileDirectory.resolve(fileName)
        toFileSystem.createDirectories(tempFileDirectory)
        Logger.d { "Download \"$fileName\" to $destination" }
        return when (val response =
            remoteStaticFileSource.downloadFile(fileName, toFileSystem.sink(destination))) {
            is Response.Error.HttpError -> {
                Logger.e { "Downloading \"$fileName\" failed: ${response.status}." }
                null
            }

            is Response.Error.Other -> {
                Logger.e(response.throwable) { "Downloading \"$fileName\" failed" }
                null
            }

            is Response.Success -> {
                Logger.d { "Downloading $fileName succeeded." }
                destination
            }
        }
    }


}
