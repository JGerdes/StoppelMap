package com.jonasgerdes.stoppelmap.model.map

import com.jonasgerdes.stoppelmap.model.map.search.SearchResult

interface InMemoryDatabase {
    fun getSearchResultById(id: String): SearchResult?
    fun setSearchResults(results: List<SearchResult>)
}


class InMemoryDatabaseImpl : InMemoryDatabase {

    private var searchResults = emptyList<SearchResult>()

    override fun getSearchResultById(id: String) = searchResults.find { it.id == id }

    override fun setSearchResults(results: List<SearchResult>) {
        searchResults = results
    }

}