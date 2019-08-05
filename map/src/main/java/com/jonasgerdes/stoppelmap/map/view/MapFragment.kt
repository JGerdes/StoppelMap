package com.jonasgerdes.stoppelmap.map.view

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.MotionScene
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.jonasgerdes.androidutil.dp
import com.jonasgerdes.androidutil.recyclerview.doOnScrolledByUser
import com.jonasgerdes.androidutil.recyclerview.doOnScrolledFinished
import com.jonasgerdes.androidutil.recyclerview.findFirstCompletelyVisibleItemPosition
import com.jonasgerdes.stoppelmap.core.routing.Route
import com.jonasgerdes.stoppelmap.core.routing.Router
import com.jonasgerdes.stoppelmap.core.util.observe
import com.jonasgerdes.stoppelmap.core.widget.BaseFragment
import com.jonasgerdes.stoppelmap.map.R
import com.jonasgerdes.stoppelmap.map.entity.Highlight
import com.jonasgerdes.stoppelmap.map.entity.MapFocus
import com.jonasgerdes.stoppelmap.map.entity.SearchResult
import com.jonasgerdes.stoppelmap.map.entity.adapter.asLatLngBounds
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMotionLayout()
        initSearch()
        initCarousel()
        initMapView(savedInstanceState)
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
            val searchResult = when (item) {
                is StallSearchResultItem -> item.result
                is TypeSearchResultItem -> item.result
                is ItemSearchResultItem -> item.result
                else -> null
            }
            if (searchResult != null) {
                Router.navigateToRoute(
                    Route.Map(Route.Map.State.Carousel(stallSlugs = searchResult.stallSlugs)),
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
                            Route.Map(Route.Map.State.Carousel(listOf(stallSlug))),
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
            }

            map.addOnCameraMoveListener {
                mapDebug.text = map.cameraPosition.toString()
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
            carouselAdapter.update(
                highlights.map { highlight ->
                    when (highlight) {
                        is Highlight.SingleStall -> StallCarouselItem(highlight)
                        is Highlight.Stalls -> TODO()
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
                    R.id.hidden -> motionLayout?.isVisible = false
                }
            }
        })
    }

    override fun processRoute(route: Route.Map) {
        when (val state = route.state) {
            is Route.Map.State.Idle -> setIdleState()
            is Route.Map.State.Search -> setSearchState()
            is Route.Map.State.Carousel -> setCarouselState(state.stallSlugs)
        }
    }

    private fun setCarouselState(stallSlugs: List<String>) {
        carouselMotion.isVisible = true
        carouselMotion.post {
            carouselMotion.transitionToState(R.id.bottom)
        }
        motionLayout.transitionToState(R.id.idle)
        searchInput.hideKeyboard()
        viewModel.onSearchResultSelected(stallSlugs)
    }

    private fun setIdleState() {
        carouselMotion.transitionToState(R.id.hidden)
        motionLayout.transitionToState(R.id.idle)
        searchInput.hideKeyboard()
        searchIcon.setOnClickListener {
            Router.navigateToRoute(Route.Map(state = Route.Map.State.Search()), Router.Destination.MAP)
        }
    }

    private fun setSearchState() {
        carouselMotion.transitionToState(R.id.hidden)
        motionLayout.transitionToState(R.id.search)
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