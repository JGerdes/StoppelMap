package com.jonasgerdes.stoppelmap.map.usecase

import com.jonasgerdes.stoppelmap.map.model.SearchResult
import com.jonasgerdes.stoppelmap.map.repository.StallRepository
import com.jonasgerdes.stoppelmap.map.repository.TypeRepository

class SearchStallsUseCase(
    private val stallRepository: StallRepository,
    private val typeRepository: TypeRepository,
) {

    suspend operator fun invoke(query: String): List<SearchResult> {
        val stallResults =
            stallRepository.findByName(query).filter { it.name.isNullOrBlank().not() }
                .map { SearchResult(it.name!!, listOf(it)) }

        val typeResults =
            typeRepository.findByName(query).map {
                SearchResult(
                    term = it.name,
                    stalls = stallRepository.findByType(it.slug)
                )
            }

        return (stallResults + typeResults).filter { it.stalls.isNotEmpty() }
    }
}
