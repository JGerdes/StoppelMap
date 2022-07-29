package com.jonasgerdes.stoppelmap.transportation.data

import com.jonasgerdes.stoppelmap.transportation.model.BusRouteSummary
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
}
