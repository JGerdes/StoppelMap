package com.jonasgerdes.stoppelmap.util.map

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import io.reactivex.Observable

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 18.06.2017
 */

fun GoogleMap.idles(): Observable<CameraPosition> {
    return MapIdleObservable(this)
}

