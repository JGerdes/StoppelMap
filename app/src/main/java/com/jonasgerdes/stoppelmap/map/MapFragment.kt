package com.jonasgerdes.stoppelmap.map

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.RecyclerView
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
import com.jonasgerdes.stoppelmap.util.*
import com.jonasgerdes.stoppelmap.util.mapbox.clicks
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.maps.MapboxMap
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Predicate
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.map_fragment.*


/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 19.04.2018
 */
class MapFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    private lateinit var flowDisposable: Disposable
    private val disposables = CompositeDisposable()
    private val state = BehaviorRelay.create<MainState>()
    private val map = BehaviorSubject.create<MapboxMap>()

    private val searchResultAdapter = SearchResultAdapter()
    private val stallCardsAdapter = StallCardAdapter()

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
            loadImages(it)
            initMapUi(it)
            initMapCamera(it)
            map.onNext(it)
        }
        search.isFocusableInTouchMode = true
        mapView.isFocusable = true
        mapView.isFocusableInTouchMode = true
        search.clearFocus()

        searchResults.adapter = searchResultAdapter
        searchResults.itemAnimator = null

        stallCards.adapter = stallCardsAdapter
        stallCards.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View?, parent: RecyclerView, state: RecyclerView.State) {
                if (stallCardsAdapter.itemCount == 1) {
                    val totalWidth = parent.width
                    val cardWidth = 256.dp
                    val sidePadding = Math.max(0, (totalWidth - cardWidth) / 2)
                    outRect.set(sidePadding, 0, sidePadding, 0)
                }
            }
        })
        LinearSnapHelper().attachToRecyclerView(stallCards)

        render(state.map { it.map }
                .observeOn(AndroidSchedulers.mainThread()))
    }


    class MarkerIcon(val name: String, val icon: Int)

    private fun loadImages(map: MapboxMap) {
        listOf(
                MarkerIcon("bar", R.drawable.ic_stall_type_bar),
                MarkerIcon("candy_stall", R.drawable.ic_stall_type_candy_stall),
                MarkerIcon("expo", R.drawable.ic_stall_type_expo),
                MarkerIcon("food_stall", R.drawable.ic_stall_type_food_stall),
                MarkerIcon("game_stall", R.drawable.ic_stall_type_game_stall),
                MarkerIcon("misc", R.drawable.ic_stall_type_info),
                MarkerIcon("parking", R.drawable.ic_stall_type_parking),
                MarkerIcon("restaurant", R.drawable.ic_stall_type_building),
                MarkerIcon("restroom", R.drawable.ic_stall_type_restroom),
                MarkerIcon("ride", R.drawable.ic_stall_type_ride),
                MarkerIcon("seller_stall", R.drawable.ic_stall_type_seller_stall)
        ).forEach {
            val color = context!!.getColorByName("marker_type_${it.name}", Color.RED)
            val bitmap = Bitmap.createBitmap(24.dp, 24.dp, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            context!!.getDrawable(R.drawable.ic_marker_outline).apply {
                bounds = canvas.clipBounds
                draw(canvas)
            }
            context!!.getDrawable(R.drawable.ic_marker_fill).apply {
                setTint(color)
                bounds = canvas.clipBounds
                draw(canvas)
            }
            context!!.getDrawable(it.icon).apply {
                setTint(Color.WHITE)
                bounds = canvas.clipBounds.insetBy(
                        left = 5.dp,
                        top = 4.dp,
                        right = 5.dp,
                        bottom = 6.dp
                )
                draw(canvas)
            }
            map.addImage(it.name, bitmap)
        }
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
        it.uiSettings.setCompassMargins(16.dp, (24 + 96).dp, 16.dp, 16.dp)
        it.uiSettings.compassImage = context?.getDrawable(R.drawable.ic_navigation_black_24dp)
                ?.apply {
                    context?.getColorCompat(R.color.colorPrimary)?.let { color -> setTint(color) }
                } ?: return
    }

    private fun bindEvents() {
        search.clicks()
                .map { MainEvent.MapEvent.SearchFieldClickedEvent() }
                .subscribe(viewModel.events) to disposables

        search.focusChanges()
                .filter { it }
                .map { MainEvent.MapEvent.SearchFieldClickedEvent() }
                .subscribe(viewModel.events) to disposables

        search.backPresses()
                .map { MainEvent.MapEvent.OnBackPressEvent() }
                .subscribe(viewModel.events) to disposables

        search.editorActionEvents(Predicate {
            !(it.keyEvent()?.action == KeyEvent.ACTION_UP
                    && it.keyEvent()?.keyCode == KeyEvent.KEYCODE_BACK)
        })
                .map { MainEvent.MapEvent.OnBackPressEvent() }
                .subscribe(viewModel.events) to disposables

        search.textChanges()
                .map { MainEvent.MapEvent.QueryEntered(it.toString()) }
                .subscribe(viewModel.events) to disposables

        clearSearch.clicks()
                .map { MainEvent.MapEvent.ClearSearchClicked }
                .subscribe(viewModel.events) to disposables

        map.distinctUntilChanged().subscribe { map ->

            val mapClicks = map.clicks().map { map.projection.toScreenLocation(it) }
                    .map { map.queryRenderedFeatures(it, "stalls") }

            mapClicks.filter { it.size > 0 }
                    .map {
                        it.let {
                            it.sortBy { it.getNumberProperty("priority")?.toInt() ?: 0 }
                            it.first()
                        }
                    }.map { it.getStringProperty("slug") }
                    .map {
                        MainEvent.MapEvent.MapItemClickedEvent(it)
                    }.subscribe(viewModel.events) to disposables

            mapClicks.filter { it.size == 0 }
                    .map { MainEvent.MapEvent.MapClickedEvent }
                    .subscribe(viewModel.events) to disposables
        } to disposables

        searchResultAdapter.selections()
                .map { MainEvent.MapEvent.SearchResultClicked(it.id) }
                .subscribe(viewModel.events) to disposables

        stallCards.itemScrolls()
                .map { MainEvent.MapEvent.StallCardSelected(it) }
                .subscribe(viewModel.events) to disposables
    }

    @SuppressLint("CheckResult")
    private fun render(state: Observable<MainState.MapState>) {
        renderSearch(activity, view, searchResultAdapter, state)
        map.subscribe {
            renderHighlight(activity, view, it, stallCardsAdapter, state)
        }
    }


    override fun onStart() {
        super.onStart()
        mapView?.onStart()
        bindEvents()
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
        disposables.clear()
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
