package com.jonasgerdes.stoppelmap.transportation.data

import com.jonasgerdes.stoppelmap.data.transportation.TransportationType
import com.jonasgerdes.stoppelmap.transportation.model.RouteSummary
import kotlinx.coroutines.flow.Flow

class TrainRoutesRepository(
    private val transportDataSource: TransportDataSource
) {

    fun getAllRoutes(): Flow<List<RouteSummary>> =
        transportDataSource.getRouteSummariesByType(TransportationType.Train)


}
