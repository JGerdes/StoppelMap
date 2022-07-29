package com.jonasgerdes.stoppelmap.transportation.data

import com.jonasgerdes.stoppelmap.transportation.model.BusRouteSummary
import kotlinx.coroutines.flow.Flow

interface TransportDataSource {
    fun getAllRoutes(): Flow<List<BusRouteSummary>>
}
