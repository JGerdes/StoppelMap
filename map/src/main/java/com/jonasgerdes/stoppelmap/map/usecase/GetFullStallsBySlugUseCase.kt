package com.jonasgerdes.stoppelmap.map.usecase

import com.jonasgerdes.stoppelmap.data.repository.StallRepository
import com.jonasgerdes.stoppelmap.model.map.Stall

class GetFullStallsBySlugUseCase(
    private val stallRepository: StallRepository
) {
    suspend operator fun invoke(slugs: List<String>): List<Stall> {
        val stalls = slugs.map { slug ->
            stallRepository.getStallBySlug(slug)
        }.filterNotNull()
        return stalls
    }
}