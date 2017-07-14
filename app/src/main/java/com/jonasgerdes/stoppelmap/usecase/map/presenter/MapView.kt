package com.jonasgerdes.stoppelmap.usecase.map.presenter

import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.jonasgerdes.stoppelmap.model.entity.map.search.MapSearchResult
import com.jonasgerdes.stoppelmap.usecase.map.viewmodel.MapBounds
import io.reactivex.Observable

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 18.06.2017
 */
interface MapView {
    fun setMapBounds(bounds: MapBounds)
    fun setMapCamera(center: LatLng, zoom: Float)

    fun setSearchField(term: String)
    fun setSearchResults(results: List<MapSearchResult>)
    fun toggleSearchResults(show: Boolean)

    fun getMapMoveEvents(): Observable<CameraPosition>
    fun getSearchEvents(): Observable<CharSequence>
    fun getSearchResultSelectionEvents(): Observable<MapSearchResult>
}