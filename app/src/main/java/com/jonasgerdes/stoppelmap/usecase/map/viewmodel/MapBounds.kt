package com.jonasgerdes.stoppelmap.usecase.map.viewmodel

import com.google.android.gms.maps.model.LatLngBounds

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 18.06.2017
 */
data class MapBounds(
        val bounds: LatLngBounds,
        val minZoom: Float,
        val maxZoom: Float
)