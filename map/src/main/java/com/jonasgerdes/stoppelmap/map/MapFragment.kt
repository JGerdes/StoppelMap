package com.jonasgerdes.stoppelmap.map

import android.os.Bundle
import android.view.View
import com.jonasgerdes.stoppelmap.core.routing.Route
import com.jonasgerdes.stoppelmap.core.routing.Router
import com.jonasgerdes.stoppelmap.core.widget.BaseFragment
import kotlinx.android.synthetic.main.fragment_map.*
import com.mapbox.mapboxsdk.maps.Style.OnStyleLoaded
import com.mapbox.mapboxsdk.maps.Style.MAPBOX_STREETS
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style


class MapFragment : BaseFragment<Route.Map>(R.layout.fragment_map) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMotionLayout()

        searchInput.onBackPress {
            Router.navigateBack()
        }

        searchCard.setOnClickListener {
            Router.navigateToRoute(Route.Map(state = Route.Map.State.Search()), Router.Destination.MAP)
        }

        initMapView(savedInstanceState)

    }

    private fun initMapView(savedInstanceState: Bundle?) {
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { mapboxMap ->
            mapboxMap.setStyle(MAPBOX_STREETS)
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
        searchInput.clearFocus()
        searchIcon.setOnClickListener {
            Router.navigateToRoute(Route.Map(state = Route.Map.State.Search()), Router.Destination.MAP)
        }
    }

    private fun setSearchState() {
        motionLayout.transitionToState(R.id.search)
        searchInput.requestFocus()
        searchInput.showKeyboard()
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
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }


}