package com.jonasgerdes.stoppelmap.util.map

import android.location.Location
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.jonasgerdes.stoppelmap.model.entity.GeoLocation
import com.jonasgerdes.stoppelmap.util.MathUtil
import io.reactivex.Observable

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 18.06.2017
 */

fun GoogleMap.idles(): Observable<CameraPosition> {
    return MapIdleObservable(this)
}

fun GoogleMap.clicks(): Observable<LatLng> {
    return MapClickObservable(this)
}

fun Location.latLng(): LatLng {
    return LatLng(latitude, longitude)
}

fun LatLng.isIn(bounds: List<GeoLocation>): Boolean {
    return MathUtil.isPointInGeoPolygon(this, bounds)
}

