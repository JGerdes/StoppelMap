package com.jonasgerdes.stoppelmap.events.usecase

import android.util.Log
import com.jonasgerdes.stoppelmap.data.repository.StallRepository
import com.jonasgerdes.stoppelmap.map.entity.FullStall

class GetStallBySlugUseCase(
    private val stallRepository: StallRepository
) {
    suspend operator fun invoke(slugs: List<String>): List<FullStall> {
        val stalls = slugs.map { slug ->
            val stall = stallRepository.getStallBySlug(slug)
            Log.d("GetFullStallUseCase","found for slug $slug stall: $stall")
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