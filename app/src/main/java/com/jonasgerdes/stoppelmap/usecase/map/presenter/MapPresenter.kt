package com.jonasgerdes.stoppelmap.usecase.map.presenter

import android.util.Log
import com.jonasgerdes.stoppelmap.model.entity.map.MapEntity
import com.jonasgerdes.stoppelmap.model.entity.map.MapMarker
import com.jonasgerdes.stoppelmap.usecase.map.viewmodel.MapInteractor
import com.jonasgerdes.stoppelmap.usecase.map.viewmodel.MapViewState
import com.jonasgerdes.stoppelmap.util.asset.Assets
import com.jonasgerdes.stoppelmap.util.plusAssign
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
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
    private var visibleEntitySubject = BehaviorSubject.create<List<MapEntity>>()

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
        disposables += view.getMapClicks()
                .subscribe(interactor::onMapClicked)
        disposables += view.getSearchEvents()
                .subscribeOn(AndroidSchedulers.mainThread())
                .map { it.toString().trim() }
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(interactor::onSearchChanged)
        disposables += view.getSearchResultSelectionEvents()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(interactor::onSearchResultSelected)

        disposables += visibleEntitySubject
                .doOnNext { Log.d("MapPresenter", "visible Entities changed:" + it.size) }
                .distinctUntilChanged { first, second -> first.size == second.size }
                .doOnNext { Log.d("MapPresenter", "not equal") }
                .map {
                    it.map {
                        MapMarker(
                                it.center.latLng,
                                it.name ?: "",
                                Assets.getTypeIconFor(it)
                        )
                    }
                }
                .subscribe(view::setMarkers)

    }

    private fun render(state: MapViewState) {
        updateMap(state)
        when (state) {
            is MapViewState.Searching -> renderSearch(state)
            is MapViewState.EntityDetail -> renderDetail(state)
            is MapViewState.Exploring -> renderExploring(state)
            else -> renderDefault(state)
        }
    }

    private fun renderExploring(state: MapViewState.Exploring) {
        renderDefault(state)
    }

    private fun renderDefault(state: MapViewState) {
        view.toggleSearchResults(false)
        //view.toggleBottomSheet(false)
    }

    private fun updateMap(state: MapViewState) {
        view.setMapBounds(state.bounds)
        view.setMapCamera(state.center, state.zoom, !isFirstMapUpdate)
        isFirstMapUpdate = false
        visibleEntitySubject.onNext(state.visibleEntities)
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