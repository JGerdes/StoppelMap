package com.jonasgerdes.stoppelmap.map.location

import co.touchlab.kermit.Logger
import com.jonasgerdes.stoppelmap.map.model.SensorLocation
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class IosLocationRepository(
    private val locationAndPermissionService: LocationAndPermissionService
) : LocationRepository {
    override fun getLocationUpdates(): Flow<SensorLocation> = callbackFlow {
        Logger.d { "📍getLocationUpdates - build callBackFlow" }
        locationAndPermissionService.locationCallback = {
            Logger.d { "📍getLocationUpdates - got location, trySend it" }
            trySend(it)
        }
        Logger.d { "📍getLocationUpdates - startLocationUpdates" }
        locationAndPermissionService.startLocationUpdates()
        awaitClose {
            Logger.d { "📍getLocationUpdates - flow closed, resetting callback and stopLocationUpdates" }
            locationAndPermissionService.locationCallback = null
            locationAndPermissionService.stopLocationUpdates()
        }
    }

    override suspend fun getLastKnownLocation(): SensorLocation? {
        return locationAndPermissionService.getLastKnownLocation()
    }
}