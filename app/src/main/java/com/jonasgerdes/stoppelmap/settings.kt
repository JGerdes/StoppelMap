package com.jonasgerdes.stoppelmap

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 16.06.2017
 */

object Settings {
    val cameraBounds = LatLngBounds(
            LatLng(52.7429499584193, 8.28653654801576),
            LatLng(52.7494011815008, 8.30059127365977)
    )
    val maxZoom = 20f
    val minZoom = 15f
    val defaultZoom = 16f
    val detailZoom = 19f
    val center = LatLng(52.74736962293996, 8.295483738183975)

    val URI_IDENTIFIER_VERSION = "2017"
    val URI_IDENTIFIER_MAP = "map"
}