package com.jonasgerdes.stoppelmap.transport.usecase

import com.jonasgerdes.stoppelmap.data.repository.RouteRepository
import com.jonasgerdes.stoppelmap.model.transportation.Route
import com.jonasgerdes.stoppelmap.model.transportation.Station

class GetRoutesUseCase(
    val routeRepository: RouteRepository
) {
    suspend operator fun invoke(): List<RouteInformation> {
        return routeRepository.getAllRoutes().map { route ->
            val stations = routeRepository.getAllStationsForRoute(route.slug).filter {
                !it.isReturnStation && it.name != "Stoppelmarkt"
            }
            val via = mutableListOf<Station>()

            via.add(stations.first())
            if (stations.size > 1) {
                via.add(stations.last())
            }
            if (stations.size > 2) {
                via.add(stations[stations.size / 2])
            }
            via.sortBy { stations.indexOf(it) }
            RouteInformation(route, via)
        }.sortedBy { it.basicInfo.name }
    }

    data class RouteInformation(
        val basicInfo: Route,
        val via: List<Station>
    )
}