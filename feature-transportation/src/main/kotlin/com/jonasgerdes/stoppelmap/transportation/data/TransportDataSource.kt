package com.jonasgerdes.stoppelmap.transportation.data

import com.jonasgerdes.stoppelmap.transportation.model.BusRoute
import com.jonasgerdes.stoppelmap.transportation.model.BusRouteSummary
import kotlinx.coroutines.flow.Flow

interface TransportDataSource {
    fun getAllRoutes(): Flow<List<BusRouteSummary>>
    fun getRouteById(id: String): Flow<BusRoute>
}
