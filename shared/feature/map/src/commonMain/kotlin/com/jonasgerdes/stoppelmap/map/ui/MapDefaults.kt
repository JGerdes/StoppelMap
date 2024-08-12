package com.jonasgerdes.stoppelmap.map.ui

import com.jonasgerdes.stoppelmap.map.model.BoundingBox
import com.jonasgerdes.stoppelmap.map.model.Location


object MapDefaults {
    val cameraBounds = BoundingBox(
        southLat = 52.7429499584193,
        westLng = 8.28653654801576,
        northLat = 52.75185363599036,
        eastLng = 8.301801681518555

    )
    const val maxZoom = 19.0
    const val minZoom = 14.0
    const val defaultZoom = 16.2
    const val detailZoom = 18.0
    val center = Location(lat = 52.7477, lng = 8.2956)

}
