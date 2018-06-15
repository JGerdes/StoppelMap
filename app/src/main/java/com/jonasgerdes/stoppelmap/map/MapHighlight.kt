package com.jonasgerdes.stoppelmap.map

import com.jonasgerdes.stoppelmap.model.map.entity.Stall
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds

sealed class MapHighlight {
    data class Center(
            val longitude: Double,
            val latitude: Double,
            val title: String
    ) : MapHighlight()

    data class Area(
            val bounds: LatLngBounds,
            val title: String
    ) : MapHighlight()

    data class MultiplePoints(
            val points: List<LatLng>,
            val title: String
    ) : MapHighlight()

    object None : MapHighlight()
}


fun Stall.highlightCenter() = MapHighlight.Center(
        longitude = centerLng,
        latitude = centerLat,
        title = name ?: this.slug
)

fun Stall.highlightArea() = MapHighlight.Area(
        LatLngBounds.from(maxLat, minLng, minLat, maxLng),
        title = name ?: this.slug
)