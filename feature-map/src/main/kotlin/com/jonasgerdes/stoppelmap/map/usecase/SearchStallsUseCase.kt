package com.jonasgerdes.stoppelmap.map.usecase

import com.jonasgerdes.stoppelmap.map.model.SearchResult
import com.jonasgerdes.stoppelmap.map.repository.AliasRepository
import com.jonasgerdes.stoppelmap.map.repository.ItemRepository
import com.jonasgerdes.stoppelmap.map.repository.StallRepository
import com.jonasgerdes.stoppelmap.map.repository.TypeRepository

class SearchStallsUseCase(
    private val stallRepository: StallRepository,
    private val typeRepository: TypeRepository,
    private val itemRepository: ItemRepository,
    private val aliasRepository: AliasRepository,
) {

    suspend operator fun invoke(query: String): List<SearchResult> {
        val stallResults =
            stallRepository.findByName(query).filter { it.name.isNullOrBlank().not() }
                .map { SearchResult(it.name!!, listOf(it)) }

        val typeResults = typeRepository.findByName(query).map {
            SearchResult(
                term = it.name,
                stalls = stallRepository.findByType(it.slug)
                        + stallRepository.findBySubType(it.slug)
            )
        }

        val itemResults = itemRepository.findByName(query).map {
            SearchResult(
                term = it.name,
                stalls = stallRepository.findByItem(it.slug)
            )
        }

        val aliasResults = aliasRepository.findByName(query).mapNotNull {
            stallRepository.getStall(it.stall)?.let { stall ->
                SearchResult(
                    term = it.alias,
                    stalls = listOf(stall)
                )
            }
        }

        return (stallResults + aliasResults + typeResults + itemResults).filter { it.stalls.isNotEmpty() }
    }
}
