package com.jonasgerdes.stoppelmap.usecase.map.view

import android.Manifest
import android.annotation.SuppressLint
import android.arch.lifecycle.LifecycleFragment
import android.arch.lifecycle.ViewModelProviders
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.TileOverlayOptions
import com.jakewharton.rxbinding2.support.v7.widget.queryTextChanges
import com.jakewharton.rxbinding2.view.clicks
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.entity.map.search.MapSearchResult
import com.jonasgerdes.stoppelmap.usecase.main.view.MainActivity
import com.jonasgerdes.stoppelmap.usecase.map.presenter.MapPresenter
import com.jonasgerdes.stoppelmap.usecase.map.presenter.MapView
import com.jonasgerdes.stoppelmap.usecase.map.view.search.SearchResultAdapter
import com.jonasgerdes.stoppelmap.usecase.map.viewmodel.MapBounds
import com.jonasgerdes.stoppelmap.usecase.map.viewmodel.MapInteractor
import com.jonasgerdes.stoppelmap.util.map.idles
import io.reactivex.Observable
import kotlinx.android.synthetic.main.map_fragment.*
import kotlin.properties.Delegates


/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 16.06.2017
 */
class MapFragment : LifecycleFragment(), MapView {
    private var map by Delegates.notNull<GoogleMap>()

    private lateinit var presenter: MapPresenter
    private val searchAdapter = SearchResultAdapter()

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.map_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val interactor = ViewModelProviders.of(activity).get(MapInteractor::class.java)
        presenter = MapPresenter(this, interactor)

        //todo: check if google play services are installed
        MapsInitializer.initialize(context)
        initMap(presenter)

        searchResultList.adapter = searchAdapter
        search.setIconifiedByDefault(false)
    }

    override fun onDestroyView() {
        presenter.dispose()
        super.onDestroyView()
    }

    @SuppressLint("MissingPermission")
    private fun initMap(presenter: MapPresenter) {
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
            map.setPadding(0, resources.getDimensionPixelSize(R.dimen.map_padding_top), 0, 0)
            map.uiSettings.isMyLocationButtonEnabled = false
            presenter.bind()

            requestLocation().subscribe {
                map.isMyLocationEnabled = true
            }
        })
    }

    @SuppressLint("MissingPermission")
    override fun getUserLocationEvents(): Observable<Location> {
        return locationFab.clicks()
                .concatMap { requestLocation() }
                .doOnNext { map.isMyLocationEnabled = true }
                .filter { map.myLocation != null }
                .map { map.myLocation }
    }

    private fun requestLocation(): Observable<Boolean> {
        return (activity as MainActivity).permissions.request(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION).filter({ it })
    }

    override fun setMapBounds(bounds: MapBounds) {
        map.setLatLngBoundsForCameraTarget(bounds.bounds)
        map.setMaxZoomPreference(bounds.maxZoom)
        map.setMinZoomPreference(bounds.minZoom)
    }

    override fun setMapCamera(center: LatLng, zoom: Float, animate: Boolean) {
        if (animate) {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(center, zoom))
        } else {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(center, zoom))
        }
    }

    override fun setSearchField(term: String) {
        search.setQuery(term, false)
    }

    override fun setSearchResults(results: List<MapSearchResult>) {
        searchAdapter.results = results
    }

    override fun toggleSearchResults(show: Boolean) {
        if (show) {
            searchResult.visibility = View.VISIBLE
        } else {
            searchResult.visibility = View.GONE
        }
    }

    override fun getMapMoveEvents(): Observable<CameraPosition> {
        return map.idles()
    }

    override fun getSearchEvents(): Observable<CharSequence> {
        return search.queryTextChanges()
    }

    override fun getSearchResultSelectionEvents(): Observable<MapSearchResult> {
        return searchAdapter.selections()
    }
}
