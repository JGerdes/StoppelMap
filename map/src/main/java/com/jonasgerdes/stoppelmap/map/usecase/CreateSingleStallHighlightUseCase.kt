package com.jonasgerdes.stoppelmap.map.usecase

import com.jonasgerdes.stoppelmap.data.repository.StallRepository
import com.jonasgerdes.stoppelmap.map.entity.Highlight
import javax.inject.Inject

class CreateSingleStallHighlightUseCase @Inject constructor(
    private val stallRepository: StallRepository,
    private val getFullStallsBySlug: GetFullStallsBySlugUseCase
) {
    suspend operator fun invoke(slug: String): Highlight {
        val fullStall = getFullStallsBySlug(listOf(slug)).firstOrNull()
            ?: throw IllegalArgumentException("no full stall found for slug $slug")
        return if (fullStall.basicInfo.name == null) {
            Highlight.NamelessStall(fullStall)
        } else {
            Highlight.SingleStall(fullStall)
        }
    }
}