package com.jonasgerdes.stoppelmap.usecase.map

import android.arch.lifecycle.LifecycleFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.TileOverlayOptions
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.Settings
import kotlin.properties.Delegates



/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 16.06.2017
 */
class MapFragment : LifecycleFragment() {

    private var map by Delegates.notNull<GoogleMap>()

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.map_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadMap()
    }

    private fun loadMap() {
        val mapFragment = SupportMapFragment.newInstance()
        childFragmentManager
                .beginTransaction()
                .replace(R.id.mapPlaceholder, mapFragment)
                .commitNowAllowingStateLoss()
        mapFragment.getMapAsync({
            map = it
            map.setLatLngBoundsForCameraTarget(Settings.cameraBounds)
            map.setMaxZoomPreference(Settings.maxZoom)
            map.setMinZoomPreference(Settings.minZoom)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(Settings.cameraBounds.center, 16f))
            map.addTileOverlay(
                    TileOverlayOptions().tileProvider(CustomMapTileProvider(resources.assets))
            )
            map.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style))
        })
    }
}