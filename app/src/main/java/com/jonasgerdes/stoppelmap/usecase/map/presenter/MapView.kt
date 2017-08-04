package com.jonasgerdes.stoppelmap.usecase.map.presenter

import android.location.Location
import android.net.Uri
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.jonasgerdes.stoppelmap.model.entity.map.MapMarker
import com.jonasgerdes.stoppelmap.model.entity.map.search.MapSearchResult
import com.jonasgerdes.stoppelmap.usecase.map.viewmodel.MapBounds
import io.reactivex.Observable

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 18.06.2017
 */
interface MapView {
    fun setMapLimits(bounds: MapBounds)
    fun setMapCamera(center: LatLng, zoom: Float, animate: Boolean = true)
    fun setMapCamera(bounds: LatLngBounds, animate: Boolean = true)

    fun setSearchField(term: String)
    fun toggleSearchFieldFocus(isFocused: Boolean)
    fun setSearchResults(results: List<MapSearchResult>)
    fun toggleSearchResults(show: Boolean)
    fun toggleMyLocationButton(show: Boolean)

    fun getMapMoveEvents(): Observable<CameraPosition>
    fun getMapClicks(): Observable<LatLng>
    fun getUserLocationEvents(): Observable<Location>
    fun getSearchEvents(): Observable<CharSequence>
    fun getSearchResultSelectionEvents(): Observable<MapSearchResult>
    fun getBottomSheetStateEvents(): Observable<Int>

    fun setMarkers(markers: List<MapMarker>)

    fun toggleBottomSheet(show: Boolean)
    fun setBottomSheetTitle(title: String)
    fun setBottomSheetImage(imageUri: Uri)
    fun setBottomSheetIcons(icons: List<Int>)

    fun showMessage(messageResource: Int)
}