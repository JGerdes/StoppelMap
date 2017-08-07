package com.jonasgerdes.stoppelmap.usecase.map.viewmodel

import com.jonasgerdes.stoppelmap.model.entity.map.MapEntity
import com.jonasgerdes.stoppelmap.model.entity.map.search.MapSearchResult
import com.jonasgerdes.stoppelmap.util.asset.Assets
import io.reactivex.Observable

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 18.06.2017
 */
sealed class MapViewState(
        val mapState: GoogleMapState,
        val visibleEntities: List<MapEntity>
) {

    class Exploring(
            mapState: GoogleMapState,
            visibleEntities: List<MapEntity>,
            val message: Int = Assets.NONE
    ) : MapViewState(mapState, visibleEntities)

    class Searching(
            mapState: GoogleMapState,
            visibleEntities: List<MapEntity>,
            val searchTerm: String,
            val results: Observable<List<MapSearchResult>>
    ) : MapViewState(mapState, visibleEntities)

    class EntityDetail(
            mapState: GoogleMapState,
            val entity: MapEntity,
            val share: Boolean = false
    ) : MapViewState(mapState, listOf(entity))

    class EntityGroupDetail(
            mapState: GoogleMapState,
            val entities: List<MapEntity>
    ) : MapViewState(mapState, entities)
}