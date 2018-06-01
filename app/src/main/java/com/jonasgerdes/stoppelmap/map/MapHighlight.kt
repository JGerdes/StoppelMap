package com.jonasgerdes.stoppelmap.map

import com.jonasgerdes.stoppelmap.model.entity.Stall

sealed class MapHighlight {
    data class Center(
            val longitude: Double,
            val latitude: Double,
            val title: String
    ): MapHighlight()

    object None: MapHighlight()
}


fun Stall.highlight() = MapHighlight.Center(
        longitude = centerLon,
        latitude = centerLat,
        title = name ?: this.slug
)