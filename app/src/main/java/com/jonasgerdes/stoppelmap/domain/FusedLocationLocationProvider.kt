package com.jonasgerdes.stoppelmap.domain

import android.content.Context
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.jonasgerdes.stoppelmap.core.domain.LocationProvider
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FusedLocationLocationProvider(context: Context) : LocationProvider {
    override suspend operator fun invoke(): LocationProvider.Location? = suspendCoroutine { continuation ->
        fusedLocationProvider.requestLocationUpdates(
            LocationRequest.create().apply {
                numUpdates = 1
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            },
            object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    continuation.resume(
                        LocationProvider.Location(
                            latitude = result.lastLocation.latitude,
                            longitude = result.lastLocation.longitude
                        )
                    )
                    fusedLocationProvider.removeLocationUpdates(this)
                }
            },
            null
        )
    }
    private val fusedLocationProvider = LocationServices.getFusedLocationProviderClient(context)
}