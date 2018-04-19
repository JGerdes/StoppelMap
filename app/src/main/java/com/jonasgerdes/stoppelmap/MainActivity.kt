package com.jonasgerdes.stoppelmap

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.jonasgerdes.stoppelmap.util.dp
import com.jonasgerdes.stoppelmap.util.getColorCompat
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.maps.MapboxMap
import kotlinx.android.synthetic.main.main_activity.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        mapView.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        mapView.setStyleUrl("asset://style-light.json")
        mapView.getMapAsync {
            initMapUi(it)
            initMapCamera(it)
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
        it.uiSettings.setCompassMargins(16.dp, (24 + 16).dp, 16.dp, 16.dp)
        it.uiSettings.compassImage = getDrawable(R.drawable.ic_navigation_black_24dp).apply {
            setTint(getColorCompat(R.color.colorPrimary))
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
        mapView.onPause()
        super.onPause()
    }

    override fun onStop() {
        mapView.onStop()
        super.onStop()
    }

    override fun onDestroy() {
        mapView.onDestroy()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        mapView.onSaveInstanceState(outState!!)
        super.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }


}
