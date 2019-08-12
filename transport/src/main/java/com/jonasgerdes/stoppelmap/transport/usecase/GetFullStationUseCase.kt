package com.jonasgerdes.stoppelmap.transport.usecase

import com.jonasgerdes.stoppelmap.data.repository.RouteRepository
import com.jonasgerdes.stoppelmap.model.transportation.Station

class GetFullStationUseCase(
    private val routeRepository: RouteRepository
) {
    suspend operator fun invoke(routeSlug: String): FullStation {
        val station = routeRepository.getStationBySlug(routeSlug)!!

        return FullStation(
            basicInfo = station
        )
    }

    data class FullStation(
        val basicInfo: Station
    )
}