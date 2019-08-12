package com.jonasgerdes.stoppelmap.transport.usecase

import com.jonasgerdes.stoppelmap.data.repository.RouteRepository
import com.jonasgerdes.stoppelmap.model.transportation.Route
import com.jonasgerdes.stoppelmap.model.transportation.Station

class GetFullRouteUseCase(
    private val routeRepository: RouteRepository
) {
    suspend operator fun invoke(routeSlug: String): FullRoute {
        val route = routeRepository.getRouteBySlug(routeSlug)!!
        val stations = routeRepository.getAllStationsForRoute(routeSlug)

        return FullRoute(
            basicInfo = route,
            stations = stations.map { StationInformation(basicInfo = it) }
        )
    }

    data class FullRoute(
        val basicInfo: Route,
        val stations: List<StationInformation>

    )

    data class StationInformation(
        val basicInfo: Station
    )
}