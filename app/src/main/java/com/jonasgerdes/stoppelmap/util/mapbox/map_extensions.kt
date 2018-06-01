package com.jonasgerdes.stoppelmap.util.mapbox

import android.location.Location
import com.jonasgerdes.stoppelmap.util.map.MapClickObservable
import com.jonasgerdes.stoppelmap.util.map.MapIdleObservable
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory.newLatLng
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import io.reactivex.Observable


fun MapboxMap.idles(): Observable<CameraPosition> {
    return MapIdleObservable(this)
}

fun MapboxMap.clicks(): Observable<LatLng> {
    return MapClickObservable(this)
}

fun toCenter(lat: Double, lng: Double) = CameraUpdateFactory.newLatLng(LatLng(lat, lng))

fun Location.latLng(): LatLng {
    return LatLng(latitude, longitude)
}

