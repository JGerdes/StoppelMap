package com.jonasgerdes.stoppelmap.usecase.map.viewmodel

import android.arch.lifecycle.ViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.jonasgerdes.stoppelmap.App
import com.jonasgerdes.stoppelmap.Settings
import com.jonasgerdes.stoppelmap.model.MapEntityRepository
import com.jonasgerdes.stoppelmap.model.entity.map.search.MapSearchResult
import com.jonasgerdes.stoppelmap.model.entity.map.search.SingleEntitySearchResult
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
            16f,
            MapBounds(
                    Settings.cameraBounds,
                    Settings.minZoom,
                    Settings.maxZoom
            )
    ))

    val state get() = stateSubject.hide()

    fun onMapMoved(position: CameraPosition) {
        stateSubject.onNext(MapViewState.Exploring(
                position.target,
                position.zoom,
                stateSubject.value.bounds)
        )
    }

    fun onSearchChanged(term: String) {
        stateSubject.onNext(MapViewState.Searching(
                stateSubject.value.center,
                stateSubject.value.zoom,
                stateSubject.value.bounds,
                term,
                repository.searchFor(term)
        ))
    }

    fun onSearchResultSelected(result: MapSearchResult) {
        when (result) {
            is SingleEntitySearchResult -> stateSubject.onNext(MapViewState.EntityDetail(
                    Settings.detailZoom,
                    stateSubject.value.bounds,
                    result.entity
            ))
        }
    }

    override fun onCleared() {
        if (!repository.isDisposed) {
            repository.dispose()
        }
        super.onCleared()
    }
}