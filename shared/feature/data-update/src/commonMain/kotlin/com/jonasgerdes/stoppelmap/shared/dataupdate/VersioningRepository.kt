package com.jonasgerdes.stoppelmap.shared.dataupdate

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.first

class VersioningRepository(
    private val dataStore: DataStore<Preferences>,
) {

    private val databaseVersionKey = intPreferencesKey("databaseVersion")
    private val mapDataVersionKey = intPreferencesKey("mapDataVersion")

    suspend fun getCurrentDatabaseVersion() = dataStore.data.first()[databaseVersionKey] ?: -1
    suspend fun getCurrentMapDataVersion() = dataStore.data.first()[mapDataVersionKey] ?: -1

    suspend fun setDatabaseVersion(version: Int) = dataStore.edit { preferences ->
        preferences[databaseVersionKey] = version
    }

    suspend fun setMapDataVersion(version: Int) = dataStore.edit { preferences ->
        preferences[mapDataVersionKey] = version
    }

}
