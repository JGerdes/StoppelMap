package com.jonasgerdes.stoppelmap.map.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import com.jonasgerdes.stoppelmap.map.model.SearchResult as FullSearchResult

private const val maxResultsToSave = 16

@OptIn(ExperimentalSerializationApi::class)
class SearchHistoryRepository(
    private val dataStore: DataStore<Preferences>
) {

    private val searchHistoryKey = stringPreferencesKey("searchHistory")

    fun getSearchHistory() = dataStore.data.map { preferences ->
        val historyJson = preferences[searchHistoryKey]
        if (historyJson == null) {
            SearchHistory(emptyList())
        } else Json.decodeFromString<SearchHistory>(historyJson)
    }

    suspend fun saveResultToHistory(result: FullSearchResult) {
        val current = getSearchHistory().first()
        val updated = SearchHistory(
            results = current.results
                .drop((current.results.size + 1 - maxResultsToSave).coerceAtLeast(0))
                .filter { it.term != result.term }
                    + result.toSearchResult()
        )
        dataStore.edit { preferences ->
            preferences[searchHistoryKey] = Json.encodeToString(updated)
        }
    }


    @Serializable
    data class SearchResult(
        val term: String,
        val stallSlugs: List<String>
    )

    @Serializable
    data class SearchHistory(
        val results: List<SearchResult>
    )

    private fun FullSearchResult.toSearchResult() = SearchResult(
        term = term,
        stallSlugs = stalls.map { it.slug }
    )
}
