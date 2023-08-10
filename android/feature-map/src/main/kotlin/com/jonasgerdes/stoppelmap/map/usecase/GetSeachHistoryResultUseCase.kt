package com.jonasgerdes.stoppelmap.map.usecase

import com.jonasgerdes.stoppelmap.map.model.SearchResult
import com.jonasgerdes.stoppelmap.map.repository.SearchHistoryRepository
import com.jonasgerdes.stoppelmap.map.repository.StallRepository
import kotlinx.coroutines.flow.map

class GetSearchHistoryUseCase(
    private val searchHistoryRepository: SearchHistoryRepository,
    private val stallRepository: StallRepository,
) {

    operator fun invoke() =
        searchHistoryRepository.getSearchHistory()
            .map { history ->
                history.results.map {
                    SearchResult(
                        term = it.term,
                        stalls = it.stallSlugs.mapNotNull { slug ->
                            stallRepository.getStall(slug)
                        },
                        type = SearchResult.Type.History
                    )
                }.reversed()
            }
}
