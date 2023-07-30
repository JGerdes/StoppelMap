package com.jonasgerdes.stoppelmap.transportation.data

import com.jonasgerdes.stoppelmap.transportation.model.Route
import com.jonasgerdes.stoppelmap.transportation.model.RouteSummary
import com.jonasgerdes.stoppelmap.transportation.model.Station
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class StaticTransportDataSource : TransportDataSource {

    private val routes by lazy { com.jonasgerdes.stoppelmap.preparation.transportation.generateBusRoutes() }

    override fun getAllRoutes(): Flow<List<RouteSummary>> = flow {
        emit(
            routes.map { route ->
                RouteSummary(
                    id = route.id,
                    title = route.title,
                    viaStations =
                    if (route.stations.size <= 4) {
                        route.stations.map { it.title }
                    } else {
                        listOf(
                            route.stations.first().title,
                            route.stations[route.stations.size / 2].title,
                            route.stations.last { it.isDestination.not() }.title,
                        )
                    }
                )
            }
        )
    }

    override fun getRouteById(id: String): Flow<Route> = flow {
        val route = routes.find { it.id == id }

        emit(route!!)
    }

    override fun getStationById(id: String): Flow<Station> = flow {
        val station = routes
            .flatMap { it.stations }
            .find { it.id == id }
            ?: routes
                .flatMap { it.returnStations }
                .find { it.id == id }

        emit(station!!)
    }
}
