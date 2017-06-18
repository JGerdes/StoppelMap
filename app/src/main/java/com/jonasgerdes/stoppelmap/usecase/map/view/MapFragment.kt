package com.jonasgerdes.stoppelmap.usecase.map.view

import android.arch.lifecycle.LifecycleFragment
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.TileOverlayOptions
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.usecase.map.presenter.MapPresenter
import com.jonasgerdes.stoppelmap.usecase.map.presenter.MapView
import com.jonasgerdes.stoppelmap.usecase.map.viewmodel.MapBounds
import com.jonasgerdes.stoppelmap.usecase.map.viewmodel.MapInteractor
import kotlin.properties.Delegates


/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 16.06.2017
 */
class MapFragment : LifecycleFragment(), MapView {

    private var map by Delegates.notNull<GoogleMap>()

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.map_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = SupportMapFragment.newInstance()
        childFragmentManager
                .beginTransaction()
                .replace(R.id.mapPlaceholder, mapFragment)
                .commitNowAllowingStateLoss()
        mapFragment.getMapAsync({
            map = it
            map.addTileOverlay(
                    TileOverlayOptions().tileProvider(CustomMapTileProvider(resources.assets))
            )
            map.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style))
            val interactor = ViewModelProviders.of(this).get(MapInteractor::class.java)
            MapPresenter(this, interactor)
        })
    }

    override fun setMapBounds(bounds: MapBounds) {
        map.setLatLngBoundsForCameraTarget(bounds.bounds)
        map.setMaxZoomPreference(bounds.maxZoom)
        map.setMinZoomPreference(bounds.minZoom)
    }

    override fun setMapCamera(center: LatLng, zoom: Float) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(center, zoom))
    }
}