package com.jonasgerdes.stoppelmap.model.entity.map

import com.google.android.gms.maps.model.LatLng

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 20.06.17
 */
data class MapMarker(
        val position: LatLng,
        val title: String,
        val iconResource: Int
)