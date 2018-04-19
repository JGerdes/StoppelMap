package com.jonasgerdes.stoppelmap

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import kotlinx.android.synthetic.main.main_activity.*



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        mapView.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        mapView.setStyleUrl("asset://style-light.json")
        mapView.getMapAsync {
            it.uiSettings.isTiltGesturesEnabled = false
            it.uiSettings.isAttributionEnabled = false

            it.setLatLngBoundsForCameraTarget(Settings.cameraBounds)
            it.setMinZoomPreference(Settings.minZoom)
            it.setMaxZoomPreference(Settings.maxZoom)

            it.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    Settings.center,
                    Settings.defaultZoom
            ))
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
