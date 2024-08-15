package com.jonasgerdes.stoppelmap.map.usecase

import com.jonasgerdes.stoppelmap.data.map.MapEntityType
import com.jonasgerdes.stoppelmap.data.shared.AliasQueries
import com.jonasgerdes.stoppelmap.map.data.MapEntityRepository
import com.jonasgerdes.stoppelmap.map.data.TagRepository
import com.jonasgerdes.stoppelmap.map.model.MapIcon
import com.jonasgerdes.stoppelmap.map.model.SearchResult
import com.jonasgerdes.stoppelmap.map.model.Tag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class GetQuickSearchSuggestionsUseCase(
    private val mapEntityRepository: MapEntityRepository,
    private val aliasQueries: AliasQueries,
    private val tagRepository: TagRepository,
) {

    operator fun invoke(): Flow<List<SearchResult>> = tagRepository.getAll()
        .map { tags ->
            listOf(
                getTagSuggestions(tags),
                getTypeSuggestions(),
            )
                .flatten()
                .sortedByDescending { it.score }
        }

    private suspend fun getTagSuggestions(tags: List<Tag>): List<SearchResult> {
        val mapEntitiesTags = mapEntityRepository.getByTag(tags.map { it.slug }.toSet())
        val mapEntityByTag = mapEntitiesTags.groupBy { it.tagSlug }
        val summaries = mapEntityRepository
            .getSummaryBySlugs(mapEntitiesTags.map { it.mapEntitySlug }.toSet())
            .associateBy { it.slug }

        return tags.mapNotNull { tag ->
            mapEntityByTag[tag.slug]
                ?.mapNotNull { summaries[it.mapEntitySlug] }
                ?.takeIf { it.isNotEmpty() }
                ?.let { summaries ->
                    SearchResult(
                        term = tag.name,
                        icon = when (tag.slug) {
                            "for_kids" -> MapIcon.Kids
                            "wheelchair_accessible" -> MapIcon.Wheelchair
                            "vegan" -> MapIcon.Plant
                            else -> summaries.groupingBy { it.icon }
                                .eachCount().entries.minByOrNull { it.value }?.key
                        },
                        score = 0.5f,
                        resultEntities = summaries,
                        type = SearchResult.Type.Collection
                    )
                }
        }
    }

    private suspend fun getTypeSuggestions() =
        listOf(
            MapEntityType.Restroom to 0.9f,
            MapEntityType.Bar to 0.4f,
            MapEntityType.Ride to 0.4f,
            MapEntityType.FoodStall to 0.4f,
            MapEntityType.CandyStall to 0.4f,
            MapEntityType.GameStall to 0.4f,
        ).mapNotNull { (type, score) ->
            val typeSummaries =
                mapEntityRepository.getSummaryBySlugs(mapEntityRepository.searchByType(type).toSet())
            val typeAlias = withContext(Dispatchers.IO) {
                aliasQueries.getByReferenceSlug(setOf(type.id)).executeAsList()
            }.maxByOrNull { it.string.length }?.string

            if (typeAlias != null && typeSummaries.isNotEmpty()) {
                SearchResult(
                    term = typeAlias,
                    icon = typeSummaries.first().icon,
                    score = score,
                    resultEntities = typeSummaries,
                    type = SearchResult.Type.Collection
                )
            } else null
        }
}