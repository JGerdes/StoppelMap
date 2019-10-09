package com.jonasgerdes.stoppelmap.map.usecase

import android.util.Log
import com.jonasgerdes.stoppelmap.data.repository.StallRepository
import com.jonasgerdes.stoppelmap.map.entity.FullStall

/**
 * Resolves a list of stall slugs to it's full representation as [FullStall]
 */
class GetFullStallsBySlugUseCase(
    private val stallRepository: StallRepository
) {
    suspend operator fun invoke(slugs: List<String>): List<FullStall> {
        val stalls = slugs.map { slug ->
            val stall = stallRepository.getStallBySlug(slug)
            stall
        }.filterNotNull().map { stall ->
            FullStall(
                basicInfo = stall,
                subTypes = stallRepository.getTypesForStall(stall.slug)
            )
        }
        return stalls
    }
}