package com.jonasgerdes.stoppelmap.map.usecase

import com.jonasgerdes.stoppelmap.data.repository.StallRepository
import com.jonasgerdes.stoppelmap.map.entity.Highlight

class CreateItemHighlightsUseCase(
    private val stallRepository: StallRepository,
    private val getFullStallsBySlug: GetFullStallsBySlugUseCase
) {
    suspend operator fun invoke(slugs: List<String>, itemSlug: String): List<Highlight> {
        val fullStalls = getFullStallsBySlug(slugs)
        val (stallsWithName, stallsWithoutName) = fullStalls.partition { it.basicInfo.name != null }

        return stallsWithName.map { Highlight.SingleStall(it) } + Highlight.ItemCollection(
            item = stallRepository.getItemBySlug(itemSlug)!!,
            stalls = stallsWithoutName
        )
    }
}