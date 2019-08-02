package com.jonasgerdes.stoppelmap.map.usecase

import com.jonasgerdes.stoppelmap.data.repository.StallRepository
import com.jonasgerdes.stoppelmap.map.entity.HighlightedText
import com.jonasgerdes.stoppelmap.map.entity.SearchResult
import com.jonasgerdes.stoppelmap.model.map.Stall

class SearchForStallsUseCase(
    private val stallRepository: StallRepository
) {

    suspend operator fun invoke(searchQuery: String): List<SearchResult> {

        val nameResults = stallRepository.queryStallsByName(searchQuery).map { stall ->
            val name = stall.name!! //save to assume here because we searched by name
            val title = HighlightedText.from(name, searchQuery)
            SearchResult.StallSearchResult(
                title = title,
                stall = stall,
                score = getScoreForStall(title, stall)
            )
        }

        val aliasResults = stallRepository.findStallByAlias(searchQuery).map { stallWithAlias ->
            val title = HighlightedText.from(stallWithAlias.alias, searchQuery)
            SearchResult.StallSearchResult(
                title = title,
                subtitle = stallWithAlias.stall.name?.let { name -> HighlightedText.withNoHighlights(name) },
                stall = stallWithAlias.stall,
                score = getScoreForStall(title, stallWithAlias.stall)
            )
        }

        return (nameResults + aliasResults)
            //sort by score first (inverted to have highest score first, if score is equal sort alphabetically
            .sortedWith(compareBy({ -it.score }, { it.title.text }))
    }

    private fun getScoreForStall(highlightedText: HighlightedText, stall: Stall): Float {
        return highlightedText.getScore() + stall.priority * 10
    }

    private fun HighlightedText.getScore(): Float {
        var score = highlights.sumBy { it.length }
        if (highlights.any { it.start == 0 }) score += 100
        return score.toFloat()
    }
}