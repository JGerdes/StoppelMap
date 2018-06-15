package com.jonasgerdes.stoppelmap.util.mapbox

import android.location.Location
import com.jonasgerdes.stoppelmap.util.map.MapClickObservable
import com.jonasgerdes.stoppelmap.util.map.MapIdleObservable
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.maps.MapboxMap
import io.reactivex.Observable


fun MapboxMap.idles(): Observable<CameraPosition> {
    return MapIdleObservable(this)
}

fun MapboxMap.clicks(): Observable<LatLng> {
    return MapClickObservable(this)
}

fun toCenter(lat: Double, lng: Double) = CameraUpdateFactory.newLatLng(LatLng(lat, lng))

fun toBounds(bounds: LatLngBounds, padding: Int = 0) =
        CameraUpdateFactory.newLatLngBounds(bounds, padding)

fun toBounds(bounds: LatLngBounds,
             left: Int = 0,
             top: Int = 0,
             right: Int = 0,
             bottom: Int = 0
) =
        CameraUpdateFactory.newLatLngBounds(bounds, left, top, right, bottom)

fun Location.latLng(): LatLng {
    return LatLng(latitude, longitude)
}

fun List<LatLng>.toBounds() = LatLngBounds.Builder().includes(this).build()