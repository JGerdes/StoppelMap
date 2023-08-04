package com.jonasgerdes.stoppelmap.transportation.data

import com.jonasgerdes.stoppelmap.data.model.database.RouteType
import com.jonasgerdes.stoppelmap.transportation.model.Route
import com.jonasgerdes.stoppelmap.transportation.model.RouteSummary
import com.jonasgerdes.stoppelmap.transportation.model.Station
import kotlinx.coroutines.flow.Flow

class TrainRoutesRepository(
    private val transportDataSource: TransportDataSource
) {

    fun getAllRoutes(): Flow<List<RouteSummary>> =
        transportDataSource.getAllRoutes(type = RouteType.Train)

    fun getRouteById(id: String): Flow<Route> = transportDataSource.getRouteById(id)
    fun getStationById(id: String): Flow<Station> = transportDataSource.getStationById(id)

}
