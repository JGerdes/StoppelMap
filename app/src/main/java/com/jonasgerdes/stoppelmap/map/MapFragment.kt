package com.jonasgerdes.stoppelmap.map

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.view.focusChanges
import com.jakewharton.rxbinding2.widget.editorActionEvents
import com.jakewharton.rxbinding2.widget.textChanges
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.Settings
import com.jonasgerdes.stoppelmap.domain.MainEvent
import com.jonasgerdes.stoppelmap.domain.MainState
import com.jonasgerdes.stoppelmap.domain.MainViewModel
import com.jonasgerdes.stoppelmap.util.dp
import com.jonasgerdes.stoppelmap.util.getColorCompat
import com.jonasgerdes.stoppelmap.util.mapbox.clicks
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.maps.MapboxMap
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Predicate
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.map_fragment.*
import java.util.concurrent.TimeUnit

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 19.04.2018
 */
class MapFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    private lateinit var flowDisposable: Disposable
    private val state = BehaviorRelay.create<MainState>()
    private val map = BehaviorSubject.create<MapboxMap>()

    private val searchResultAdapter = SearchResultAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.map_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("MapFragment", "onViewCreated()")
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)

        mapView.setStyleUrl("asset://style-light.json")
        mapView.getMapAsync {
            initMapUi(it)
            initMapCamera(it)
            map.onNext(it)
        }
        search.isFocusableInTouchMode = true
        mapView.isFocusable = true
        mapView.isFocusableInTouchMode = true
        search.clearFocus()

        searchResults.adapter = searchResultAdapter

        bindEvents()

        render(state.map { it.map }
                .observeOn(AndroidSchedulers.mainThread()))
    }

    private fun initMapCamera(it: MapboxMap) {
        it.setLatLngBoundsForCameraTarget(Settings.cameraBounds)
        it.setMinZoomPreference(Settings.minZoom)
        it.setMaxZoomPreference(Settings.maxZoom)
        it.moveCamera(CameraUpdateFactory.newLatLngZoom(
                Settings.center,
                Settings.defaultZoom
        ))
    }

    private fun initMapUi(it: MapboxMap) {
        it.uiSettings.isTiltGesturesEnabled = false
        it.uiSettings.isAttributionEnabled = false
        it.uiSettings.isLogoEnabled = false
        it.uiSettings.setCompassMargins(16.dp, (24 + 64).dp, 16.dp, 16.dp)
        it.uiSettings.compassImage = context?.getDrawable(R.drawable.ic_navigation_black_24dp)
                ?.apply {
                    context?.getColorCompat(R.color.colorPrimary)?.let { color -> setTint(color) }
                }
    }

    @SuppressLint("CheckResult")
    private fun bindEvents() {
        search.clicks()
                .map { MainEvent.MapEvent.SearchFieldClickedEvent() }
                .subscribe(viewModel.events)

        search.focusChanges()
                .filter { it }
                .map { MainEvent.MapEvent.SearchFieldClickedEvent() }
                .subscribe(viewModel.events)

        search.backPresses()
                .map { MainEvent.MapEvent.OnBackPressEvent() }
                .subscribe(viewModel.events)

        search.editorActionEvents(Predicate {
            !(it.keyEvent()?.action == KeyEvent.ACTION_UP
                    && it.keyEvent()?.keyCode == KeyEvent.KEYCODE_BACK)
        })
                .map { MainEvent.MapEvent.OnBackPressEvent() }
                .subscribe(viewModel.events)

        search.textChanges()
                .map { MainEvent.MapEvent.QueryEntered(it.toString()) }
                .subscribe(viewModel.events)

        map.subscribe { map ->
            map.clicks().map { map.projection.toScreenLocation(it) }
                    .map { map.queryRenderedFeatures(it, "stalls") }
                    .filter { it.size > 0 }
                    .map {
                        it.let {
                            it.sortBy { it.getNumberProperty("priority")?.toInt() ?: 0 }
                            it.first()
                        }
                    }.map { it.getStringProperty("slug") }
                    .map {
                        MainEvent.MapEvent.MapItemClickedEvent(it)
                    }.subscribe(viewModel.events)
        }

        searchResultAdapter.selections
                .map { it.id }
                .map { MainEvent.MapEvent.MapItemClickedEvent(it) }
                .subscribe(viewModel.events)
    }

    @SuppressLint("CheckResult")
    private fun render(state: Observable<MainState.MapState>) {
        renderSearch(activity, view, searchResultAdapter, state)
        map.subscribe {
            renderHighlight(activity, view, it, state)
        }
    }


    override fun onStart() {
        super.onStart()
        mapView?.onStart()
        flowDisposable = viewModel.state.subscribe(state)
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        mapView?.onPause()
        super.onPause()
    }

    override fun onStop() {
        mapView?.onStop()
        flowDisposable.dispose()
        super.onStop()
    }

    override fun onDestroyView() {
        mapView?.onDestroy()
        super.onDestroyView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

}
