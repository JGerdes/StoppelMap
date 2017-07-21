package com.jonasgerdes.stoppelmap.usecase.map.viewmodel

import com.google.android.gms.maps.model.LatLng
import com.jonasgerdes.stoppelmap.model.entity.map.MapEntity
import com.jonasgerdes.stoppelmap.model.entity.map.search.MapSearchResult
import io.reactivex.Observable

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 18.06.2017
 */
sealed class MapViewState(
        val center: LatLng,
        val zoom: Float,
        val bounds: MapBounds,
        val visibleEntities: List<MapEntity>
) {

    class Exploring(
            center: LatLng,
            zoom: Float,
            bounds: MapBounds,
            visibleEntities: List<MapEntity>
    ) : MapViewState(center, zoom, bounds, visibleEntities)

    class Searching(
            center: LatLng,
            zoom: Float,
            bounds: MapBounds,
            visibleEntities: List<MapEntity>,
            val searchTerm: String,
            val results: Observable<List<MapSearchResult>>
    ) : MapViewState(center, zoom, bounds, visibleEntities)

    class EntityDetail(
            zoom: Float,
            bounds: MapBounds,
            visibleEntities: List<MapEntity>,
            val entity: MapEntity
    ) : MapViewState(entity.center.latLng, zoom, bounds, visibleEntities)
}