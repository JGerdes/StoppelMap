package com.jonasgerdes.stoppelmap.model.entity.map

import com.google.android.gms.maps.model.LatLngBounds
import com.jonasgerdes.stoppelmap.model.entity.GeoLocation
import com.jonasgerdes.stoppelmap.model.entity.Picture

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 14.07.17
 */

fun MapEntity.getPictures(type: String): List<Picture> {
    return pictures.filter { it.type == type }
}

fun MapEntity.Companion.boundsFor(entities: List<MapEntity>): LatLngBounds {
    val boundBuilder = LatLngBounds.builder()
    entities.map(MapEntity::center)
            .map(GeoLocation::latLng)
            .forEach { boundBuilder.include(it) }
    return boundBuilder.build()
}