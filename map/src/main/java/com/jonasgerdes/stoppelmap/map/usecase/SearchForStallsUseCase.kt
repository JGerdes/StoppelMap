package com.jonasgerdes.stoppelmap.map.usecase

import com.jonasgerdes.stoppelmap.data.repository.StallRepository
import com.jonasgerdes.stoppelmap.map.entity.HighlightedText
import com.jonasgerdes.stoppelmap.map.entity.SearchResult

class SearchForStallsUseCase(
    private val stallRepository: StallRepository
) {

    suspend operator fun invoke(searchQuery: String): List<SearchResult> {

        val nameResults = stallRepository.queryStallsByName(searchQuery).map { stall ->
            val name = stall.name!! //save to assume here because we searched by name
            SearchResult.StallSearchResult(
                title = HighlightedText.from(name, searchQuery),
                stall = stall
            )
        }


        return (nameResults).sortedByDescending { it.score }
    }
}