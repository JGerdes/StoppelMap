package com.jonasgerdes.stoppelmap.map.view

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import com.jonasgerdes.androidutil.dp
import com.jonasgerdes.androidutil.recyclerview.doOnScrolledByUser
import com.jonasgerdes.stoppelmap.core.routing.Route
import com.jonasgerdes.stoppelmap.core.routing.Router
import com.jonasgerdes.stoppelmap.core.util.observe
import com.jonasgerdes.stoppelmap.core.widget.BaseFragment
import com.jonasgerdes.stoppelmap.map.R
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
    private var map: MapboxMap? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMotionLayout()
        initSearch()
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
                else -> null
            }
            if (searchResult != null) viewModel.onSearchResultSelected(searchResult)
            setIdleState() //TODO: do this via Router
        }

        observe(viewModel.searchResults) { searchResults ->
            searchResultAdapter.update(searchResults.map { result ->
                when (result) {
                    is SearchResult.StallSearchResult -> StallSearchResultItem(result)
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
                        viewModel.onStallClicked(stallSlug)
                        true
                    } else false

                }
            }

            map.addOnCameraMoveListener {
                mapDebug.text = map.cameraPosition.toString()
            }
        }
        observe(viewModel.mapFocus) { focus ->
            val cameraUpdate = when (focus) {
                is MapFocus.All -> CameraUpdateFactory.newLatLngBounds(focus.coordinates.asLatLngBounds(), 64.dp)
            }
            map?.animateCamera(cameraUpdate)
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

    override fun processRoute(route: Route.Map) {
        when (route.state) {
            is Route.Map.State.Idle -> setIdleState()
            is Route.Map.State.Search -> setSearchState()
        }
    }

    private fun setIdleState() {
        motionLayout.transitionToState(R.id.idle)
        searchInput.hideKeyboard()
        searchIcon.setOnClickListener {
            Router.navigateToRoute(Route.Map(state = Route.Map.State.Search()), Router.Destination.MAP)
        }
    }

    private fun setSearchState() {
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