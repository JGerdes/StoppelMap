package com.jonasgerdes.stoppelmap.map

import com.mapbox.geojson.Point
import com.mapbox.maps.CoordinateBounds

object MapDefaults {
    val cameraBounds = CoordinateBounds(
        Point.fromLngLat(
            8.28653654801576,
            52.7429499584193
        ),
        Point.fromLngLat(
            8.301801681518555,
            52.75185363599036
        )
    )
    const val maxZoom = 19.0
    const val minZoom = 14.0
    const val defaultZoom = 15.2
    const val detailZoom = 18.0
    val center = Point.fromLngLat(8.295345, 52.748351)

}
