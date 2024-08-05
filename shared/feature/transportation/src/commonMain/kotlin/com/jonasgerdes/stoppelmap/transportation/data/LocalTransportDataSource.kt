package com.jonasgerdes.stoppelmap.transportation.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import com.jonasgerdes.stoppelmap.data.transportation.RouteQueries
import com.jonasgerdes.stoppelmap.data.transportation.StationQueries
import com.jonasgerdes.stoppelmap.data.transportation.TransportationType
import com.jonasgerdes.stoppelmap.transportation.model.DetailedRoute
import com.jonasgerdes.stoppelmap.transportation.model.RouteSummary
import com.jonasgerdes.stoppelmap.transportation.model.StationSummary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow

class TransportDataSource(
    private val routeQueries: RouteQueries,
    private val stationQueries: StationQueries,
) {
    fun getRouteSummariesByType(type: TransportationType): Flow<List<RouteSummary>> =
        routeQueries.getBasicByType(type, ::RouteSummary)
            .asFlow()
            .mapToList(Dispatchers.IO)

    fun getDetailedRouteBySlug(routeSlug: String): Flow<DetailedRoute> =
        routeQueries.getDetailedBySlug(routeSlug, ::DetailedRoute)
            .asFlow()
            .mapToOne(Dispatchers.IO)

    fun getStationSummariesByRoute(routeSlug: String): Flow<List<StationSummary>> =
        stationQueries.getBasicByRoute(routeSlug, ::StationSummary)
            .asFlow()
            .mapToList(Dispatchers.IO)

}