package com.jonasgerdes.stoppelmap.transportation.data

import com.jonasgerdes.stoppelmap.data.model.database.RouteType
import com.jonasgerdes.stoppelmap.transportation.model.ExtendedStation
import com.jonasgerdes.stoppelmap.transportation.model.Route
import com.jonasgerdes.stoppelmap.transportation.model.RouteSummary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class DummyTransportDataSource : TransportDataSource {
    override fun getAllRoutes(type: RouteType?): Flow<List<RouteSummary>> = emptyFlow()

    override fun getRouteById(id: String): Flow<Route> = emptyFlow()

    override fun getStationById(id: String): Flow<ExtendedStation> = emptyFlow()
}