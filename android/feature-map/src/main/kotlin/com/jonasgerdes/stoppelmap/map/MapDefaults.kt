package com.jonasgerdes.stoppelmap.map

import org.maplibre.android.geometry.LatLng
import org.maplibre.android.geometry.LatLngBounds

object MapDefaults {
    val cameraBounds = LatLngBounds.fromLatLngs(
        listOf(
            LatLng(
                52.7429499584193,
                8.28653654801576,
            ),
            LatLng(
                52.75185363599036,
                8.301801681518555,
            )
        )
    )
    const val maxZoom = 19.0
    const val minZoom = 14.0
    const val defaultZoom = 16.2
    const val detailZoom = 18.0
    val center = LatLng(latitude = 52.7477, longitude = 8.2956)

}
