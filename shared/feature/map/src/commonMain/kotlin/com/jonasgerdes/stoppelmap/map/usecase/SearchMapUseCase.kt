package com.jonasgerdes.stoppelmap.map.usecase

import com.jonasgerdes.stoppelmap.data.map.MapEntityType
import com.jonasgerdes.stoppelmap.map.data.MapEntityRepository
import com.jonasgerdes.stoppelmap.map.data.OfferRepository
import com.jonasgerdes.stoppelmap.map.data.SubTypeRepository
import com.jonasgerdes.stoppelmap.map.model.SearchResult

class SearchMapUseCase(
    val mapEntityRepository: MapEntityRepository,
    val subTypeRepository: SubTypeRepository,
    val offerRepository: OfferRepository,
) {
    suspend operator fun invoke(query: String): List<SearchResult> {
        val cleanQuery = query.trim().lowercase()
        return listOf(
            searchByName(cleanQuery),
            searchByAlias(cleanQuery),
            searchByType(cleanQuery),
            searchBySubType(cleanQuery),
            searchByProduct(cleanQuery),
        ).flatten().sortedBy { it.term }
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

    private suspend fun searchByAlias(query: String): List<SearchResult> {
        val byAlias = mapEntityRepository.searchByAlias(query)
        val summaries = mapEntityRepository.getSummaryBySubType(byAlias.map { it.slug }.toSet())

        return byAlias.mapNotNull {
            summaries[it.slug]?.let { summaries ->
                SearchResult(
                    term = it.alias,
                    icon = summaries.first().icon,
                    score = 0.4f,
                    resultEntities = summaries,
                    type = SearchResult.Type.Collection,
                )
            }
        }
    }

    private suspend fun searchByType(query: String): List<SearchResult> {
        val typeAliases = mapEntityRepository.searchAliasBy(MapEntityType.entries.map { it.id }.toSet(), query)

        return typeAliases.mapNotNull { typeAlias ->
            MapEntityType.entries.firstOrNull { typeAlias.referenceSlug == it.id }?.let { type ->
                val summaries = mapEntityRepository.getSummaryBySlugs(mapEntityRepository.searchByType(type).toSet())

                if (summaries.isEmpty()) null
                else SearchResult(
                    term = typeAlias.string,
                    icon = summaries.first().icon,
                    score = 0.6f,
                    resultEntities = summaries,
                    type = SearchResult.Type.Collection

                )
            }
        }
    }

    private suspend fun searchBySubType(query: String): List<SearchResult> {
        val byName = subTypeRepository.searchByName(query)
        val byNameSummaries = mapEntityRepository.getSummaryBySubType(byName.map { it.slug }.toSet())
        val byAlias = subTypeRepository.searchByAlias(query)
        val byAliasSummaries = mapEntityRepository.getSummaryBySubType(byAlias.map { it.slug }.toSet())

        return byName.mapNotNull {
            byNameSummaries[it.slug]?.let { summaries ->
                SearchResult(
                    term = it.name,
                    icon = summaries.first().icon,
                    score = 0.2f,
                    resultEntities = summaries,
                    type = SearchResult.Type.Collection,
                )
            }
        } + byAlias.mapNotNull {
            byAliasSummaries[it.slug]?.let { summaries ->
                SearchResult(
                    term = it.alias,
                    icon = summaries.first().icon,
                    score = 0.2f,
                    resultEntities = summaries,
                    type = SearchResult.Type.Collection,
                )
            }
        }
    }

    private suspend fun searchByProduct(query: String): List<SearchResult> {
        val products = offerRepository.searchProducts(query)
        val mapEntitiesProducts = offerRepository.getMapEntitiesOffering(products.map { it.slug }.toSet())
        val mapEntityByProduct = mapEntitiesProducts.groupBy { it.productSlug }
        val summaries = mapEntityRepository
            .getSummaryBySlugs(mapEntitiesProducts.map { it.mapEntitySlug }.toSet())
            .associateBy { it.slug }

        return products.mapNotNull { product ->
            mapEntityByProduct[product.slug]
                ?.mapNotNull { summaries[it.mapEntitySlug] }
                ?.takeIf { it.isNotEmpty() }
                ?.let { summaries ->
                    SearchResult(
                        term = product.name,
                        icon = summaries.groupingBy { it.icon }.eachCount().entries.minByOrNull { it.value }?.key,
                        score = 0.1f,
                        resultEntities = summaries,
                        type = SearchResult.Type.Collection
                    )
                }
        }
    }
}