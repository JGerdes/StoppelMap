package com.jonasgerdes.stoppelmap.map.usecase

import com.jonasgerdes.stoppelmap.data.repository.StallRepository
import com.jonasgerdes.stoppelmap.map.entity.Highlight

class CreateTypeHighlightsUseCase(
    private val stallRepository: StallRepository,
    private val getFullStallsBySlug: GetFullStallsBySlugUseCase
) {
    suspend operator fun invoke(slugs: List<String>, typeSlug: String): List<Highlight> {
        val fullStalls = getFullStallsBySlug(slugs)
        val (stallsWithName, stallsWithoutName) = fullStalls.partition { it.basicInfo.name != null }

        return stallsWithName.map { Highlight.SingleStall(it) } + Highlight.TypeCollection(
            type = stallRepository.getTypeBySlug(typeSlug)!!,
            stalls = stallsWithoutName
        )
    }
}