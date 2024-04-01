package com.jonasgerdes.stoppelmap.schedule.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.map

class BookmarkedEventsRepository(
    private val dataStore: DataStore<Preferences>,
) {

    private val bookmarkedEventKey = stringSetPreferencesKey("bookmarkedEvent")

    fun getBookmarkedEvents() = dataStore.data.map { it[bookmarkedEventKey] ?: emptySet() }

    suspend fun addBookmarkedEvent(stationId: String) {
        dataStore.edit {
            it[bookmarkedEventKey] = (it[bookmarkedEventKey] ?: emptySet()) + stationId
        }
    }

    suspend fun removeBookmarkedEvent(stationId: String) {
        dataStore.edit {
            it[bookmarkedEventKey] = (it[bookmarkedEventKey] ?: emptySet()) - stationId
        }
    }

}
