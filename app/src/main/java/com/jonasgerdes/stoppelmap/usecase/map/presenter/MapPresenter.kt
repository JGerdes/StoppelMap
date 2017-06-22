package com.jonasgerdes.stoppelmap.usecase.map.presenter

import com.jonasgerdes.stoppelmap.usecase.map.viewmodel.MapInteractor
import com.jonasgerdes.stoppelmap.usecase.map.viewmodel.MapViewState
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 18.06.2017
 */
class MapPresenter(
        private val view: MapView,
        private val interactor: MapInteractor) {

    fun bind() {
        interactor.state
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::render)
        view.getMapMoveEvents()
                .subscribeOn(AndroidSchedulers.mainThread())
                .distinct()
                .doOnNext(interactor::onMapMoved)
                .subscribe()
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