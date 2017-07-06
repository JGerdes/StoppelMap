package com.jonasgerdes.stoppelmap.usecase.map.viewmodel

import com.google.android.gms.maps.model.LatLng
import com.jonasgerdes.stoppelmap.model.entity.map.search.MapSearchResult
import io.reactivex.Observable

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 18.06.2017
 */
sealed class MapViewState(
        val center: LatLng,
        val zoom: Float,
        val bounds: MapBounds
) {

    class Exploring(
            center: LatLng,
            zoom: Float,
            bounds: MapBounds
    ) : MapViewState(center, zoom, bounds)

    class Searching(
            center: LatLng,
            zoom: Float,
            bounds: MapBounds,
            val searchTerm: String,
            val results: Observable<List<MapSearchResult>>
    ) : MapViewState(center, zoom, bounds)
}