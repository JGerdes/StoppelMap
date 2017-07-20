package com.jonasgerdes.stoppelmap.usecase.map.presenter

import com.jonasgerdes.stoppelmap.model.entity.map.MapMarker
import com.jonasgerdes.stoppelmap.usecase.map.viewmodel.MapInteractor
import com.jonasgerdes.stoppelmap.usecase.map.viewmodel.MapViewState
import com.jonasgerdes.stoppelmap.util.Assets
import com.jonasgerdes.stoppelmap.util.plusAssign
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 18.06.2017
 */
class MapPresenter(
        private val view: MapView,
        private val interactor: MapInteractor) : Disposable {

    private val disposables = CompositeDisposable()
    private var isFirstMapUpdate = true //don't animate first map update

    fun bind() {
        disposables += interactor.state
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::render)

        disposables += view.getMapMoveEvents()
                .subscribeOn(AndroidSchedulers.mainThread())
                .distinct()
                .doOnNext(interactor::onMapMoved)
                .subscribe()
        disposables += view.getUserLocationEvents()
                .subscribe(interactor::onUserMoved)
        disposables += view.getSearchEvents()
                .subscribeOn(AndroidSchedulers.mainThread())
                .map { it.toString().trim() }
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(interactor::onSearchChanged)
        disposables += view.getSearchResultSelectionEvents()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(interactor::onSearchResultSelected)

    }

    private fun render(state: MapViewState) {
        updateMap(state)
        when (state) {
            is MapViewState.Searching -> renderSearch(state)
            is MapViewState.EntityDetail -> renderDetail(state)
            else -> renderDefault(state)
        }
    }

    private fun renderDefault(state: MapViewState) {
        view.toggleSearchResults(false)
        //view.toggleBottomSheet(false)
    }

    private fun updateMap(state: MapViewState) {
        view.setMapBounds(state.bounds)
        view.setMapCamera(state.center, state.zoom, !isFirstMapUpdate)
        isFirstMapUpdate = false
    }

    private fun renderSearch(state: MapViewState.Searching) {
        view.setSearchField(state.searchTerm)
        //view.toggleBottomSheet(false)
        disposables += state.results.subscribe {
            view.toggleSearchResults(!it.isEmpty())
            view.setSearchResults(it)
        }
    }

    private fun renderDetail(state: MapViewState.EntityDetail) {
        state.entity.name?.let { view.setSearchField(it) }
        view.toggleSearchResults(false)
        view.toggleSearchFieldFocus(false)

        state.entity.name?.let { view.setBottomSheetTitle(it) }
        view.setBottomSheetImage(Assets.getHeadersFor(state.entity)[0])
        view.setMarkers(listOf(MapMarker(
                state.entity.center.latLng,
                state.entity.name ?: "",
                Assets.getTypeIconFor(state.entity)

        )))
        //workaround: wait 500ms so keyboard is actually closed before showing bottom sheet
        Completable.complete()
                .observeOn(AndroidSchedulers.mainThread())
                .delay(500, TimeUnit.MILLISECONDS)
                .subscribe {
                    view.toggleBottomSheet(true)
                }
    }

    override fun isDisposed(): Boolean {
        return disposables.isDisposed
    }

    override fun dispose() {
        disposables.dispose()
    }
}