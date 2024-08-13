package com.jonasgerdes.stoppelmap.map.repository.location

import com.jonasgerdes.stoppelmap.map.model.SensorLocation
import android.location.Location as AndroidLocation

fun AndroidLocation.toLocation() = SensorLocation(
    latitude = latitude,
    longitude = longitude,
    accuracyMeters = accuracy
)

fun SensorLocation.toAndroidLocation() = AndroidLocation("Fused").apply {
    latitude = this@toAndroidLocation.latitude
    longitude = this@toAndroidLocation.longitude
    accuracy = this@toAndroidLocation.accuracyMeters
}