package com.jonasgerdes.stoppelmap.map.usecase

import com.jonasgerdes.stoppelmap.data.repository.StallRepository
import com.jonasgerdes.stoppelmap.map.entity.FullStall

class GetFullStallsBySlugUseCase(
    private val stallRepository: StallRepository
) {
    suspend operator fun invoke(slugs: List<String>): List<FullStall> {
        val stalls = slugs.map { slug ->
            stallRepository.getStallBySlug(slug)
        }.filterNotNull().map { stall ->
            FullStall(
                basicInfo = stall,
                subTypes = stallRepository.getTypesForStall(stall.slug)
            )
        }
        return stalls
    }
}