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
                stall = stall,
                score = 100f
            )
        }

        val aliasResults = stallRepository.findStallByAlias(searchQuery).map { stallWithAlias ->
            SearchResult.StallSearchResult(
                title = HighlightedText.from(stallWithAlias.alias, searchQuery),
                subtitle = stallWithAlias.stall.name?.let { name -> HighlightedText.from(name, searchQuery) },
                stall = stallWithAlias.stall,
                score = 80f
            )
        }

        return (nameResults + aliasResults).sortedByDescending { it.score }
    }
}