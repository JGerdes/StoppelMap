package com.jonasgerdes.stoppelmap.map.view

import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds

object Settings {
    val cameraBounds = LatLngBounds.Builder()
            .include(LatLng(52.7429499584193, 8.28653654801576))
            .include(LatLng(52.7494011815008, 8.30059127365977))
            .build()
    const val maxZoom = 20.0
    const val minZoom = 14.0
    const val defaultZoom = 15.2
    const val detailZoom = 18.0
    val center = LatLng(52.74736962293996, 8.295483738183975)

}