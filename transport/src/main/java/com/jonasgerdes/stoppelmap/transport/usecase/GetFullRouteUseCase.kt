package com.jonasgerdes.stoppelmap.transport.usecase

import com.jonasgerdes.stoppelmap.core.domain.DateTimeProvider
import com.jonasgerdes.stoppelmap.data.repository.RouteRepository
import com.jonasgerdes.stoppelmap.model.transportation.Route
import com.jonasgerdes.stoppelmap.model.transportation.Station
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject

private const val NEXT_DEPARTURE_COUNT = 3

class GetFullRouteUseCase @Inject constructor(
    private val routeRepository: RouteRepository,
    private val getCurrentTime: DateTimeProvider
) {
    suspend operator fun invoke(routeSlug: String): FullRoute {
        val route = routeRepository.getRouteBySlug(routeSlug)!!
        val stations = routeRepository.getAllStationsForRoute(routeSlug)

        val now = getCurrentTime()
        return FullRoute(
            basicInfo = route,
            stations = stations.filter {
                it.name != "Stoppelmarkt" && !it.isReturnStation
            }.map { station ->
                StationInformation(
                    basicInfo = station,
                    nextDepartures = routeRepository.getAllDeparturesForStationAfter(
                        station.slug, now, NEXT_DEPARTURE_COUNT
                    ).map { it.time }
                )
            },
            returnStation = stations.first { it.isReturnStation }
        )
    }

    data class FullRoute(
        val basicInfo: Route,
        val stations: List<StationInformation>,
        val returnStation: Station

    )

    data class StationInformation(
        val basicInfo: Station,
        val nextDepartures: List<OffsetDateTime>
    )
}