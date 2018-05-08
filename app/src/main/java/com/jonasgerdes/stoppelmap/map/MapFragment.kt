package com.jonasgerdes.stoppelmap.map

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.Settings
import com.jonasgerdes.stoppelmap.domain.MainViewModel
import com.jonasgerdes.stoppelmap.util.dp
import com.jonasgerdes.stoppelmap.util.getColorCompat
import com.jonasgerdes.stoppelmap.util.toggleLayoutFulscreen
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.maps.MapboxMap
import kotlinx.android.synthetic.main.map_fragment.*

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 19.04.2018
 */
class MapFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.map_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)
        activity?.toggleLayoutFulscreen(true)

        mapView.setStyleUrl("asset://style-light.json")
        mapView.getMapAsync {
            initMapUi(it)
            initMapCamera(it)
        }
        mapView.isFocusable = true
        mapView.isFocusableInTouchMode = true
        search.clearFocus()
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

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
        search.clearFocus()
    }

    override fun onPause() {
        mapView?.onPause()
        super.onPause()
    }

    override fun onStop() {
        mapView?.onStop()
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