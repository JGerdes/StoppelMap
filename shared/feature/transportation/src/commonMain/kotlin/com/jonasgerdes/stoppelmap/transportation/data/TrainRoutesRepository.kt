package com.jonasgerdes.stoppelmap.transportation.data

import com.jonasgerdes.stoppelmap.data.model.database.RouteType
import com.jonasgerdes.stoppelmap.transportation.model.ExtendedStation
import com.jonasgerdes.stoppelmap.transportation.model.Route
import com.jonasgerdes.stoppelmap.transportation.model.RouteSummary
import kotlinx.coroutines.flow.Flow

class TrainRoutesRepository(
    private val transportDataSource: TransportDataSource
) {

    fun getAllRoutes(): Flow<List<RouteSummary>> =
        transportDataSource.getAllRoutes(type = RouteType.Train)

    fun getRouteById(id: String): Flow<Route> = transportDataSource.getRouteById(id)
    fun getStationById(id: String): Flow<ExtendedStation> = transportDataSource.getStationById(id)

}
