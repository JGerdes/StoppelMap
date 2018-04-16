package com.jonasgerdes.stoppelmap

import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 16.04.2018
 */
object Settings {
    val cameraBounds = LatLngBounds.Builder()
            .include(LatLng(52.7429499584193, 8.28653654801576))
            .include(LatLng(52.7494011815008, 8.30059127365977))
            .build()
    val maxZoom = 20.0
    val minZoom = 15.0
    val defaultZoom = 15.2
    val detailZoom = 19.0
    val center = LatLng(52.74736962293996, 8.295483738183975)

}