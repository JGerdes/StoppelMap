package com.jonasgerdes.stoppelmap.usecase.map.viewmodel

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 04.08.17
 */
data class GoogleMapState(
        val limits: MapBounds,
        val center: LatLng? = null,
        val zoom: Float? = 18f,
        val bounds: LatLngBounds? = null
)