package com.jonasgerdes.stoppelmap.dataupdate

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.first

class VersioningRepository(
    private val dataStore: DataStore<Preferences>,
) {

    private val databaseVersionKey = intPreferencesKey("databaseVersion")

    suspend fun getCurrentDatabaseVersion() = dataStore.data.first()[databaseVersionKey] ?: -1

    suspend fun setDatabaseVersion(version: Int) = dataStore.edit { preferences ->
        preferences[databaseVersionKey] = version
    }

}
