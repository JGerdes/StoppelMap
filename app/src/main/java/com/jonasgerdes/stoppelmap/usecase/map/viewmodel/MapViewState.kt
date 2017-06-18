package com.jonasgerdes.stoppelmap.usecase.map.viewmodel

import com.google.android.gms.maps.model.LatLng

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 18.06.2017
 */
sealed class MapViewState {

    data class Exploring(
            val center: LatLng,
            val zoom: Float,
            val bounds: MapBounds
    ) : MapViewState()
}