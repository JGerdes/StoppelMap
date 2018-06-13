package com.jonasgerdes.stoppelmap.model.map

import com.jonasgerdes.stoppelmap.model.map.search.SearchResult

interface InMemoryDatabase {
    fun getSearchResultById(id: String): SearchResult?
    fun setSearchResults(results: List<SearchResult>)

    fun getStallCard(index: Int): StallCard?
    fun setStallCards(cards: List<StallCard>)
}


class InMemoryDatabaseImpl : InMemoryDatabase {

    private var searchResults = emptyList<SearchResult>()
    private var stallCards = emptyList<StallCard>()

    override fun getSearchResultById(id: String) = searchResults.find { it.id == id }

    override fun setSearchResults(results: List<SearchResult>) {
        searchResults = results.toList()
    }

    override fun getStallCard(index: Int) = stallCards.getOrNull(index)

    override fun setStallCards(cards: List<StallCard>) {
        stallCards = cards.toList()
    }

}