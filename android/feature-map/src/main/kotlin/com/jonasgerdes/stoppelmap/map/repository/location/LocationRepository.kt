package com.jonasgerdes.stoppelmap.map.repository.location

import android.location.Location
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority
import kotlinx.coroutines.flow.Flow

interface LocationRepository {

    suspend fun getLastKnownLocation(): Location?
    fun getLocationUpdates(
        locationRequest: LocationRequest = LocationRequest.create()
            .setInterval(5000)
            .setSmallestDisplacement(3f)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
    ): Flow<Location>
}
