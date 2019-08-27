package com.jonasgerdes.stoppelmap.map.view

import android.Manifest
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.MotionScene
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.jonasgerdes.androidutil.dp
import com.jonasgerdes.androidutil.recyclerview.doOnScrolledByUser
import com.jonasgerdes.androidutil.recyclerview.doOnScrolledFinished
import com.jonasgerdes.androidutil.recyclerview.findFirstCompletelyVisibleItemPosition
import com.jonasgerdes.stoppelmap.core.routing.Route
import com.jonasgerdes.stoppelmap.core.routing.Route.Map.State.Carousel.StallCollection
import com.jonasgerdes.stoppelmap.core.routing.Router
import com.jonasgerdes.stoppelmap.core.util.observe
import com.jonasgerdes.stoppelmap.core.widget.BaseFragment
import com.jonasgerdes.stoppelmap.map.R
import com.jonasgerdes.stoppelmap.map.entity.*
import com.jonasgerdes.stoppelmap.map.entity.adapter.asLatLngBounds
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_map.*
import org.koin.android.ext.android.inject


class MapFragment : BaseFragment<Route.Map>(R.layout.fragment_map) {

    private val viewModel: MapViewModel by inject()
    private val searchResultAdapter = GroupAdapter<ViewHolder>()
    private val carouselAdapter = GroupAdapter<ViewHolder>()
    private var map: MapboxMap? = null
    private var routeToProcess: Route.Map? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMotionLayout()
        initSearch()
        initCarousel()
        initMapView(savedInstanceState)
    }

    private val shareStallListener = { slug: String, name: String ->
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.map_share_stall_title, slug))
        }
        val uri = getString(R.string.map_share_legacy_uri, slug)
        val message = getString(R.string.map_share_stall_text, name, uri)
        shareIntent.putExtra(Intent.EXTRA_TEXT, message)
        startActivity(Intent.createChooser(shareIntent, getString(R.string.map_share_chooser_title, name)))
    }

    private fun initSearch() {
        searchInput.apply {
            onBackPress { Router.navigateBack() }

            doOnTextChanged { text, start, count, after ->
                if (text != null) viewModel.onSearchEntered(text.toString())
            }
        }

        searchCard.setOnClickListener {
            Router.navigateToRoute(Route.Map(state = Route.Map.State.Search()), Router.Destination.MAP)
        }

        searchResultList.adapter = searchResultAdapter

        searchResultList.doOnScrolledByUser {
            searchInput.hideKeyboard()
        }

        searchResultAdapter.setOnItemClickListener { item, view ->
            val stallCollection = when (item) {
                is StallSearchResultItem -> StallCollection.Single(item.result.stall.slug)
                is TypeSearchResultItem -> StallCollection.TypeCollection(item.result.type.slug, item.result.stallSlugs)
                is ItemSearchResultItem -> StallCollection.ItemCollection(item.result.item.slug, item.result.stallSlugs)
                else -> null
            }
            if (stallCollection != null) {
                Router.navigateToRoute(
                    Route.Map(Route.Map.State.Carousel(stallCollection)),
                    Router.Destination.MAP
                )
            }
        }

        observe(viewModel.searchResults) { searchResults ->
            searchResultAdapter.update(searchResults.map { result ->
                when (result) {
                    is SearchResult.StallSearchResult -> StallSearchResultItem(result)
                    is SearchResult.TypeSearchResult -> TypeSearchResultItem(result)
                    is SearchResult.ItemSearchResult -> ItemSearchResultItem(result)
                }
            })
        }

        observe(viewModel.message) { message ->
            val messageString = when (message) {
                is Message.NotInArea -> getString(R.string.map_message_not_in_area)
            }
            Toast.makeText(context, messageString, Toast.LENGTH_SHORT).show()

        }

        locationFab.setOnClickListener {
            assurePermissionAndShowUserLocation(wasTriggeredManually = true)
        }
    }

    private fun assurePermissionAndShowUserLocation(wasTriggeredManually: Boolean = false) {
        if (PermissionsManager.areLocationPermissionsGranted(context)) {
            enableUserLocation()
            viewModel.onCenterOnUserTriggered()
        } else askPermission(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION) {
            enableUserLocation()
            viewModel.onCenterOnUserTriggered()
        }.onDeclined { result ->
            if (wasTriggeredManually && result.hasForeverDenied()) {
                result.goToSettings();
            }
        }
    }

    private fun enableUserLocation() {
        map?.let { map ->
            map.locationComponent.apply {
                activateLocationComponent(LocationComponentActivationOptions.builder(context!!, map.style!!).build())
                isLocationComponentEnabled = true
                renderMode = RenderMode.NORMAL
            }
        }
    }

    private fun initMapView(savedInstanceState: Bundle?) {
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { map ->
            val styleBuilder = Style.Builder().fromUri("asset://style-light.json")
            initMapUi(map)
            initMapCamera(map)
            map.setStyle(styleBuilder) { style ->
                loadImages(context!!, style)
                this@MapFragment.map = map

                map.addOnMapClickListener { location ->
                    val pointOnMap = map.projection.toScreenLocation(location)
                    val stallSlug = map.queryRenderedFeatures(pointOnMap, "stalls")
                        .sortedByDescending { it.getNumberProperty("priority")?.toInt() ?: 0 }
                        .firstOrNull()?.getStringProperty("slug")
                    if (stallSlug != null) {
                        Router.navigateToRoute(
                            Route.Map(Route.Map.State.Carousel(StallCollection.Single(stallSlug))),
                            Router.Destination.MAP
                        )
                        true
                    } else {
                        Router.navigateToRoute(
                            Route.Map(Route.Map.State.Idle()),
                            Router.Destination.MAP
                        )
                        false
                    }
                }

                routeToProcess?.let {
                    processRouteImplementation(it)
                    routeToProcess = null
                }

                assurePermissionAndShowUserLocation(wasTriggeredManually = true)
            }
        }
        observe(viewModel.mapFocus) { focus ->
            val cameraUpdate = when (focus) {
                is MapFocus.All -> CameraUpdateFactory.newLatLngBounds(
                    focus.coordinates.asLatLngBounds(),
                    64.dp,
                    80.dp,
                    64.dp,
                    208.dp
                )
                is MapFocus.One -> CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        focus.coordinate.latitude, focus.coordinate.longitude
                    ),
                    Settings.detailZoom
                )
                MapFocus.None -> null
            }
            if (cameraUpdate != null) {
                map?.animateCamera(cameraUpdate)
            }
        }
    }

    private fun initMotionLayout() {
        motionLayout.setOnApplyWindowInsetsListener { v, insets ->
            listOf(
                R.id.idle,
                R.id.search
            ).forEach {
                motionLayout.getConstraintSet(it).setGuidelineBegin(R.id.guideTop, insets.systemWindowInsetTop)
            }
            motionLayout.updateState()
            insets
        }
        motionLayout.requestApplyInsets()
    }

    private fun initCarousel() {
        LinearSnapHelper().attachToRecyclerView(stallCarousel)
        //TODO: improve this
        stallCarousel.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                if (carouselAdapter.itemCount == 1) {
                    val totalWidth = parent.width
                    val cardWidth = 256.dp + 16.dp
                    val sidePadding = Math.max(0, (totalWidth - cardWidth) / 2)
                    outRect.set(sidePadding, 0, sidePadding, 0)
                }
            }
        })
        stallCarousel.adapter = carouselAdapter
        observe(viewModel.highlightedStalls) { highlights ->
            map?.setMarkers(highlights.flatMap { it.getStalls() }.map { stall ->
                MarkerItem(
                    LatLng(
                        stall.basicInfo.centerLat,
                        stall.basicInfo.centerLng
                    ),
                    stall.basicInfo.type.type,
                    stall.basicInfo.name
                )
            })
            val isOnlyOne = highlights.size == 1
            carouselAdapter.clear()
            carouselAdapter.addAll(
                highlights.map { highlight ->
                    when (highlight) {
                        is Highlight.SingleStall -> StallCarouselItem(highlight, shareStallListener)
                        is Highlight.TypeCollection -> TypeCollectionCarouselItem(highlight, isOnlyOne)
                        is Highlight.ItemCollection -> ItemCollectionCarouselItem(highlight, isOnlyOne)
                        is Highlight.NamelessStall -> NamelessStallCarouselItem(highlight)
                    }
                })
        }

        stallCarousel.doOnScrolledFinished {
            val id = stallCarousel.findFirstCompletelyVisibleItemPosition()
            if (id != -1) {
                viewModel.onStallHighlightedSelected((carouselAdapter.getItem(id) as CarouselItem).highlight)
            }
        }

        carouselMotion.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) = Unit
            override fun allowsTransition(p0: MotionScene.Transition?) = true
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) = Unit
            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) = Unit
            override fun onTransitionCompleted(motionLayout: MotionLayout?, transition: Int) {
                when (transition) {
                    R.id.hidden -> {
                        motionLayout?.isVisible = false
                        viewModel.onHighlightsHidden()
                        locationFab.show()
                    }
                }
            }
        })
    }

    override fun processRoute(route: Route.Map) {
        if (map == null) {
            routeToProcess = route
        } else processRouteImplementation(route)
    }

    fun processRouteImplementation(route: Route.Map) {
        if (map == null) {
            //TODO: log error, something went really wrong here
            return
        }
        when (val state = route.state) {
            is Route.Map.State.Idle -> setIdleState()
            is Route.Map.State.Search -> setSearchState()
            is Route.Map.State.Carousel -> setCarouselState(state.stallCollection)
        }
    }

    private fun setCarouselState(stallSlugs: StallCollection) {
        carouselMotion.isVisible = true
        locationFab.hide()
        carouselMotion.post {
            carouselMotion.transitionToState(R.id.bottom)
        }
        motionLayout.transitionToState(R.id.idle)
        searchInput.hideKeyboard()
        viewModel.onStallsSelected(stallSlugs)
    }

    private fun setIdleState() {
        carouselMotion.transitionToState(R.id.hidden)
        motionLayout.transitionToState(R.id.idle)
        locationFab.show()
        searchInput.hideKeyboard()
        searchIcon.setOnClickListener {
            Router.navigateToRoute(Route.Map(state = Route.Map.State.Search()), Router.Destination.MAP)
        }
    }

    private fun setSearchState() {
        carouselMotion.transitionToState(R.id.hidden)
        motionLayout.transitionToState(R.id.search)
        locationFab.hide()
        searchInput.post {
            searchInput.requestFocus()
            searchInput.showKeyboard()
        }
        searchIcon.setOnClickListener {
            Router.navigateBack()
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}