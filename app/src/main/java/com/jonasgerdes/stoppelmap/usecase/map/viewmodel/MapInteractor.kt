package com.jonasgerdes.stoppelmap.usecase.map.viewmodel

import android.arch.lifecycle.ViewModel
import com.jonasgerdes.stoppelmap.Settings
import io.reactivex.subjects.BehaviorSubject

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 18.06.2017
 */
class MapInteractor : ViewModel() {
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
}