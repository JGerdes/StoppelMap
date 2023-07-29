package com.jonasgerdes.stoppelmap.map.repository.location

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationTokenSource
import com.jonasgerdes.stoppelmap.map.repository.PermissionRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import kotlin.coroutines.resume

class LocationRepositoryImpl(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val permissionRepository: PermissionRepository,
) : LocationRepository {

    @SuppressLint("MissingPermission")
    override suspend fun getLastKnownLocation(): Location? =
        suspendCancellableCoroutine { continuation ->
            if (!permissionRepository.isLocationPermissionGranted()) {
                Timber.d("lastLocation no permission!")
                continuation.resume(null)
            } else {
                val cancellationToken = CancellationTokenSource()
                fusedLocationProviderClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    cancellationToken.token
                ).apply {
                    addOnSuccessListener {
                        Timber.d("lastLocation success: $it")
                        continuation.resume(it)
                    }
                    addOnFailureListener {
                        Timber.w("lastLocation failed: $it")
                        continuation.resume(null)
                    }
                    addOnCanceledListener {
                        Timber.w("lastLocation canceled")
                        continuation.cancel()
                    }
                    continuation.invokeOnCancellation {
                        Timber.w("lastLocation invokeOnCancellation")
                        cancellationToken.cancel()
                    }
                }
            }
        }

    override fun getLocationUpdates(
        locationRequest: LocationRequest
    ): Flow<Location> = permissionRepository.getLocationPermissionState()
        .flatMapLatest { isGranted ->
            if (isGranted) getLocationUpdatesInternal(locationRequest) else flowOf()
        }


    @SuppressLint("MissingPermission")
    private fun getLocationUpdatesInternal(locationRequest: LocationRequest): Flow<Location> =
        channelFlow {
            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    result.locations.forEach { trySend(it) }
                }
            }
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                null
            )

            awaitClose {
                fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            }
        }
}
