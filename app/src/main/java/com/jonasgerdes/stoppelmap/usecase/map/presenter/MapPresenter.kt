package com.jonasgerdes.stoppelmap.usecase.map.presenter

import com.jonasgerdes.stoppelmap.usecase.map.viewmodel.MapInteractor
import com.jonasgerdes.stoppelmap.usecase.map.viewmodel.MapViewState

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 18.06.2017
 */
class MapPresenter(
        private val view: MapView,
        private val interactor: MapInteractor) {

    init {
        interactor.state.subscribe(this::render)
    }

    private fun render(state: MapViewState) {
        when (state) {
            is MapViewState.Exploring -> renderExploring(state)
        }
    }

    private fun renderExploring(state: MapViewState.Exploring) {
        view.setMapBounds(state.bounds)
        view.setMapCamera(state.center, state.zoom)
    }
}