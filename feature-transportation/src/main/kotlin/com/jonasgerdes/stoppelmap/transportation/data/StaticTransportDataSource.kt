package com.jonasgerdes.stoppelmap.transportation.data

import com.jonasgerdes.stoppelmap.transportation.model.BusRoute
import com.jonasgerdes.stoppelmap.transportation.model.BusRouteSummary
import com.jonasgerdes.stoppelmap.transportation.model.Station
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class StaticTransportDataSource : TransportDataSource {

    private val routes by lazy { generateBusRoutes() }

    override fun getAllRoutes(): Flow<List<BusRouteSummary>> = flow {
        emit(
            routes.map { route ->
                BusRouteSummary(
                    id = route.id,
                    title = route.title,
                    viaStations = listOf(
                        route.stations.first().title,
                        route.stations[route.stations.size / 2].title,
                        route.stations.last { it.isDestination.not() }.title,
                    )
                )
            }
        )
    }

    override fun getRouteById(id: String): Flow<BusRoute> = flow {
        val route = routes.find { it.id == id }

        emit(route!!)
    }

    override fun getStationById(id: String): Flow<Station> = flow {
        val station = routes
            .flatMap { it.stations }
            .find { it.id == id }

        emit(station!!)
    }
}
