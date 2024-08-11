package com.jonasgerdes.stoppelmap.map.usecase

import com.jonasgerdes.stoppelmap.map.data.MapEntityRepository
import com.jonasgerdes.stoppelmap.map.model.SearchResult

class SearchMapUseCase(
    val mapEntityRepository: MapEntityRepository,
) {
    suspend operator fun invoke(query: String): List<SearchResult> {
        val cleanQuery = query.trim().lowercase()
        return listOf(
            searchByName(cleanQuery)
        ).flatten()
    }


    private suspend fun searchByName(query: String): List<SearchResult> {
        val resultSlugs = mapEntityRepository.searchMapEntitiesByName(query)
        return mapEntityRepository.getSummaryBySlugs(resultSlugs.toSet())
            .map {
                SearchResult(
                    term = it.name,
                    icon = it.icon,
                    score = 0.5f,
                    resultEntities = listOf(it),
                    type = SearchResult.Type.SingleStall
                )
            }
    }
}