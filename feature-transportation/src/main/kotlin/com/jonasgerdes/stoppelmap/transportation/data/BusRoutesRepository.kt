package com.jonasgerdes.stoppelmap.transportation.data

import com.jonasgerdes.stoppelmap.transportation.model.BusRoute
import com.jonasgerdes.stoppelmap.transportation.model.BusRouteSummary
import com.jonasgerdes.stoppelmap.transportation.model.Station
import kotlinx.coroutines.flow.Flow

class BusRoutesRepository(
    private val transportDataSource: TransportDataSource
) {

    fun getAllRoutes(): Flow<List<BusRouteSummary>> = transportDataSource.getAllRoutes()
    fun getRouteById(id: String): Flow<BusRoute> = transportDataSource.getRouteById(id)
    fun getStationById(id: String): Flow<Station> = transportDataSource.getStationById(id)

}
