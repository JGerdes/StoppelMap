package com.jonasgerdes.stoppelmap.usecase.map.presenter

import android.util.Log
import com.jonasgerdes.stoppelmap.usecase.map.viewmodel.MapInteractor
import com.jonasgerdes.stoppelmap.usecase.map.viewmodel.MapViewState
import com.jonasgerdes.stoppelmap.util.plusAssign
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 18.06.2017
 */
class MapPresenter(
        private val view: MapView,
        private val interactor: MapInteractor) : Disposable {

    private val disposables = CompositeDisposable()

    fun bind() {
        disposables += interactor.state
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::render)
        disposables += view.getMapMoveEvents()
                .subscribeOn(AndroidSchedulers.mainThread())
                .distinct()
                .doOnNext(interactor::onMapMoved)
                .subscribe()
        disposables += view.getSearchEvents()
                .subscribeOn(AndroidSchedulers.mainThread())
                .map { it.toString().trim() }
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(interactor::onSearchChanged)
    }

    private fun render(state: MapViewState) {
        updateMap(state)
        when (state) {
            is MapViewState.Searching -> renderSearch(state)
            else -> renderDefault(state)
        }
    }

    private fun renderDefault(state: MapViewState) {
        view.toggleSearchResults(false)
    }

    private fun updateMap(state: MapViewState) {
        view.setMapBounds(state.bounds)
        view.setMapCamera(state.center, state.zoom)
    }

    private fun renderSearch(state: MapViewState.Searching) {
        view.setSearchField(state.searchTerm)
        disposables += state.results.subscribe {
            Log.d("MapPresenter", "\n\nresults:")
            it.forEach {
                Log.d("MapPresenter", it.title)
            }
            view.toggleSearchResults(!it.isEmpty())
            view.setSearchResults(it)
        }
    }

    override fun isDisposed(): Boolean {
        return disposables.isDisposed
    }

    override fun dispose() {
        disposables.dispose()
    }
}