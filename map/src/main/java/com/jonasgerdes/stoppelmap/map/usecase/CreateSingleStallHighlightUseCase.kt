package com.jonasgerdes.stoppelmap.map.usecase

import com.jonasgerdes.stoppelmap.data.repository.StallRepository
import com.jonasgerdes.stoppelmap.map.entity.Highlight

class CreateSingleStallHighlightUseCase(
    private val stallRepository: StallRepository,
    private val getFullStallsBySlug: GetFullStallsBySlugUseCase
) {
    suspend operator fun invoke(slug: String): Highlight {
        val fullStall = getFullStallsBySlug(listOf(slug)).first()
        return if (fullStall.basicInfo.name == null) {
            Highlight.NamelessStall(fullStall)
        } else {
            Highlight.SingleStall(fullStall)
        }
    }
}