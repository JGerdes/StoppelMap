package com.jonasgerdes.stoppelmap.shared.dataupdate.source.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.jonasgerdes.stoppelmap.dto.config.RemoteAppConfig
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class LocalAppConfigSource(
    private val dataStore: DataStore<Preferences>,
    private val json: Json,
) {

    private val appConfigCacheKey = stringPreferencesKey("appConfigCache")

    fun getAppConfig() = dataStore.data.map {
        runCatching {
            it[appConfigCacheKey]?.takeIf { it.isNotBlank() }?.let { jsonString ->
                json.decodeFromString<RemoteAppConfig>(jsonString)
            }
        }.getOrNull()
    }

    suspend fun storeAppConfig(appConfig: RemoteAppConfig) {
        dataStore.edit {
            it[appConfigCacheKey] = runCatching { json.encodeToString(appConfig) }.getOrElse { "" }
        }
    }
}
