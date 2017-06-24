package com.jonasgerdes.stoppelmap.usecase.map.viewmodel

import android.arch.lifecycle.ViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.jonasgerdes.stoppelmap.App
import com.jonasgerdes.stoppelmap.Settings
import com.jonasgerdes.stoppelmap.model.MapEntityRepository
import io.reactivex.subjects.BehaviorSubject
import kotlin.properties.Delegates

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 18.06.2017
 */
class MapInteractor : ViewModel() {

    private var repository by Delegates.notNull<MapEntityRepository>()

    init {
        App.graph.inject(this)
    }

    private val stateSubject = BehaviorSubject.createDefault(MapViewState.Exploring(
            Settings.cameraBounds.center,
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

    override fun onCleared() {
        if (!repository.isDisposed) {
            repository.dispose()
        }
        super.onCleared()
    }
}