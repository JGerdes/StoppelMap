package com.jonasgerdes.stoppelmap.map.location

import com.jonasgerdes.stoppelmap.map.model.SensorLocation
import kotlinx.coroutines.flow.Flow

interface LocationRepository {

    fun getLocationUpdates(): Flow<SensorLocation>

    suspend fun getLastKnownLocation(): SensorLocation?
}