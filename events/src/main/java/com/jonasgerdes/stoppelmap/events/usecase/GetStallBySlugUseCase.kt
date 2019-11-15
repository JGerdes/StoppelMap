package com.jonasgerdes.stoppelmap.events.usecase

import com.jonasgerdes.stoppelmap.data.repository.StallRepository
import com.jonasgerdes.stoppelmap.model.map.Stall
import javax.inject.Inject

class GetStallBySlugUseCase @Inject constructor(
    private val stallRepository: StallRepository
) {
    suspend operator fun invoke(slug: String): Stall? {
        return stallRepository.getStallBySlug(slug)
    }
}