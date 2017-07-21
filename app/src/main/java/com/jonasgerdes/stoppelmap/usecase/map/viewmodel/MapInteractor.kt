package com.jonasgerdes.stoppelmap.usecase.map.viewmodel

import android.arch.lifecycle.ViewModel
import android.location.Location
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.jonasgerdes.stoppelmap.App
import com.jonasgerdes.stoppelmap.Settings
import com.jonasgerdes.stoppelmap.model.MapEntityRepository
import com.jonasgerdes.stoppelmap.model.entity.map.search.MapSearchResult
import com.jonasgerdes.stoppelmap.model.entity.map.search.SingleEntitySearchResult
import com.jonasgerdes.stoppelmap.util.map.latLng
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 18.06.2017
 */
class MapInteractor : ViewModel() {

    @Inject
    protected lateinit var repository: MapEntityRepository

    init {
        App.graph.inject(this)
    }

    private val stateSubject = BehaviorSubject.createDefault<MapViewState>(MapViewState.Exploring(
            Settings.center,
            Settings.defaultZoom,
            MapBounds(
                    Settings.cameraBounds,
                    Settings.minZoom,
                    Settings.maxZoom
            ),
            repository.getVisibleEntities(Settings.defaultZoom)

    ))

    val state get() = stateSubject.hide()

    fun onMapMoved(position: CameraPosition) {
        stateSubject.onNext(
                when (stateSubject.value) {
                    is MapViewState.EntityDetail -> MapViewState.EntityDetail(
                            position.zoom,
                            stateSubject.value.bounds,
                            repository.getVisibleEntities(position.zoom),
                            (stateSubject.value as MapViewState.EntityDetail).entity,
                            position.target
                    )
                    else -> MapViewState.Exploring(
                            position.target,
                            position.zoom,
                            stateSubject.value.bounds,
                            repository.getVisibleEntities(position.zoom))

                })
    }

    fun onUserMoved(location: Location) {
        stateSubject.onNext(MapViewState.Exploring(
                location.latLng(),
                stateSubject.value.zoom,
                stateSubject.value.bounds,
                repository.getVisibleEntities(stateSubject.value.zoom))
        )
    }

    fun onMapClicked(position: LatLng) {
        val entity = repository.getEntityOn(position)
        stateSubject.onNext(
                if (entity != null) {
                    MapViewState.EntityDetail(
                            Settings.detailZoom,
                            stateSubject.value.bounds,
                            stateSubject.value.visibleEntities,
                            entity
                    )
                } else {
                    MapViewState.Exploring(
                            stateSubject.value.center,
                            stateSubject.value.zoom,
                            stateSubject.value.bounds,
                            stateSubject.value.visibleEntities)
                }
        )
    }

    fun onSearchChanged(term: String) {
        stateSubject.onNext(MapViewState.Searching(
                stateSubject.value.center,
                stateSubject.value.zoom,
                stateSubject.value.bounds,
                stateSubject.value.visibleEntities,
                term,
                repository.searchFor(term)
        ))
    }

    fun onSearchResultSelected(result: MapSearchResult) {
        when (result) {
            is SingleEntitySearchResult -> stateSubject.onNext(MapViewState.EntityDetail(
                    Settings.detailZoom,
                    stateSubject.value.bounds,
                    stateSubject.value.visibleEntities,
                    result.entity
            ))
        }
    }

    fun onBottomSheetClosed(state: Int) {
        stateSubject.onNext(MapViewState.Exploring(
                stateSubject.value.center,
                stateSubject.value.zoom,
                stateSubject.value.bounds,
                stateSubject.value.visibleEntities))
    }

    override fun onCleared() {
        if (!repository.isDisposed) {
            repository.dispose()
        }
        super.onCleared()
    }
}