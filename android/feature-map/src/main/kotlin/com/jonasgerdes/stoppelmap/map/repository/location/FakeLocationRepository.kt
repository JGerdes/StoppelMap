package com.jonasgerdes.stoppelmap.map.repository.location

import android.location.Location
import com.google.android.gms.location.LocationRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class FakeLocationRepository(
    private val startLocation: Location,
    private val endLocation: Location? = null,
    private val duration: Duration = 1.toDuration(DurationUnit.MINUTES),
) : LocationRepository {
    private var lastKnownLocation: Location = startLocation
    override suspend fun getLastKnownLocation(): Location? = lastKnownLocation

    private val stepDuration = 5.toDuration(DurationUnit.SECONDS)

    override fun getLocationUpdates(locationRequest: LocationRequest): Flow<Location> = flow {
        emit(startLocation)
        lastKnownLocation = startLocation
        if (endLocation != null) {
            val steps = (duration / stepDuration).toInt()
            for (i in 0 until steps) {
                delay(stepDuration)
                val newLocation = lerp(startLocation, endLocation, i / steps.toFloat())
                emit(newLocation)
                lastKnownLocation = newLocation
            }
            delay(stepDuration)
            emit(endLocation)
            lastKnownLocation = endLocation
        }
    }

    private fun lerp(start: Location, stop: Location, fraction: Float) =
        Location(start).apply {
            latitude = lerp(start.latitude, stop.latitude, fraction)
            longitude = lerp(start.longitude, stop.longitude, fraction)
            altitude = lerp(start.altitude, stop.altitude, fraction)
            bearing = lerp(start.bearing, stop.bearing, fraction)
            accuracy = lerp(start.accuracy, stop.accuracy, fraction)
        }

    private fun lerp(first: Double, second: Double, fraction: Float) =
        first + (second - first) * fraction

    private fun lerp(first: Float, second: Float, fraction: Float) =
        first + (second - first) * fraction
}
