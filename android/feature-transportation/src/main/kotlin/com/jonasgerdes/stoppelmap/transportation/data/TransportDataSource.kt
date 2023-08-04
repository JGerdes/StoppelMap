package com.jonasgerdes.stoppelmap.transportation.data

import com.jonasgerdes.stoppelmap.data.model.database.RouteType
import com.jonasgerdes.stoppelmap.transportation.model.Route
import com.jonasgerdes.stoppelmap.transportation.model.RouteSummary
import com.jonasgerdes.stoppelmap.transportation.model.Station
import kotlinx.coroutines.flow.Flow

interface TransportDataSource {
    fun getAllRoutes(type: RouteType? = null): Flow<List<RouteSummary>>
    fun getRouteById(id: String): Flow<Route>
    fun getStationById(id: String): Flow<Station>
}
