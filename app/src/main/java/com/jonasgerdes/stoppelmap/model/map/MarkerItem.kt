package com.jonasgerdes.stoppelmap.model.map

import com.jonasgerdes.stoppelmap.util.mapbox.toBounds
import com.mapbox.mapboxsdk.geometry.LatLng


data class MarkerItem(val location: LatLng, val type: String)

fun List<MarkerItem>.toBounds() = map { it.location }.toBounds()!!