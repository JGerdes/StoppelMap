package com.jonasgerdes.stoppelmap.map.model

data class SensorLocation(
    val latitude: Double,
    val longitude: Double,
    val accuracyMeters: Float,
)

fun SensorLocation.toLocation() = Location(lat = latitude, lng = longitude)