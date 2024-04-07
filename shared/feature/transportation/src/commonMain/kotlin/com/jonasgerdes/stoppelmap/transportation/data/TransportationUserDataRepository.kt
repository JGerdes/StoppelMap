package com.jonasgerdes.stoppelmap.transportation.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.map

class TransportationUserDataRepository(
    private val dataStore: DataStore<Preferences>,
) {

    private val favouriteStationsKey = stringSetPreferencesKey("favouriteStations")

    fun getFavouriteStations() = dataStore.data.map { it[favouriteStationsKey] ?: emptySet() }

    suspend fun addFavouriteStations(stationId: String) {
        dataStore.edit {
            it[favouriteStationsKey] = (it[favouriteStationsKey] ?: emptySet()) + stationId
        }
    }

    suspend fun removeFavouriteStations(stationId: String) {
        dataStore.edit {
            it[favouriteStationsKey] = (it[favouriteStationsKey] ?: emptySet()) - stationId
        }
    }

}
