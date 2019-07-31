package com.jonasgerdes.stoppelmap.map.entity.adapter

import com.jonasgerdes.stoppelmap.map.entity.Location
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds

fun List<Location>.asLatLngBounds() = fold(LatLngBounds.Builder()) { builder, location ->
    builder.include(LatLng(location.latitude, location.longitude))
}.build()