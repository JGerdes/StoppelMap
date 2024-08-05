package com.jonasgerdes.stoppelmap.transportation.data

import com.jonasgerdes.stoppelmap.data.transportation.TransportationType
import com.jonasgerdes.stoppelmap.transportation.model.RouteSummary
import com.jonasgerdes.stoppelmap.transportation.model.StationSummary
import kotlinx.coroutines.flow.Flow

class BusRoutesRepository(
    private val transportDataSource: TransportDataSource
) {
    fun getRouteSummaries(): Flow<List<RouteSummary>> =
        transportDataSource.getRouteSummariesByType(TransportationType.Bus)

    fun getDetailedRoute(routeSlug: String) = transportDataSource.getDetailedRouteBySlug(routeSlug)

    fun getStationSummaries(routeSlug: String): Flow<List<StationSummary>> =
        transportDataSource.getStationSummariesByRoute(routeSlug)

}
