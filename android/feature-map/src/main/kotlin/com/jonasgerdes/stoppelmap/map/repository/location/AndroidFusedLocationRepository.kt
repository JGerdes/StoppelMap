package com.jonasgerdes.stoppelmap.map.repository.location

import android.annotation.SuppressLint
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.jonasgerdes.stoppelmap.map.location.LocationRepository
import com.jonasgerdes.stoppelmap.map.model.SensorLocation
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import kotlin.coroutines.resume

class AndroidFusedLocationRepository(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
) : LocationRepository {

    @SuppressLint("MissingPermission")
    override suspend fun getLastKnownLocation(): SensorLocation? =
        suspendCancellableCoroutine { continuation ->
            val cancellationToken = CancellationTokenSource()
            fusedLocationProviderClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                cancellationToken.token
            ).apply {
                addOnSuccessListener {
                    Timber.d("lastLocation success: $it")
                    continuation.resume(it.toLocation())
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


    @SuppressLint("MissingPermission")
    override fun getLocationUpdates(): Flow<SensorLocation> =
        callbackFlow {
            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    result.locations.forEach {
                        Timber.d("new location: $it")
                        trySend(it.toLocation())
                    }
                }
            }
            fusedLocationProviderClient.requestLocationUpdates(
                LocationRequest.Builder(5_000)
                    .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                    .setWaitForAccurateLocation(false)
                    .build(),
                locationCallback,
                Looper.getMainLooper(),
            ).addOnFailureListener {
                close(it)
            }

            awaitClose {
                fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            }
        }
}
