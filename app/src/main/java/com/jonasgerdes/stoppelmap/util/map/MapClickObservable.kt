package com.jonasgerdes.stoppelmap.util.map

import android.os.Looper
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 20.07.2017
 */

class MapClickObservable(val map: GoogleMap) : Observable<LatLng>() {
    override fun subscribeActual(observer: Observer<in LatLng>?) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            val message = "Not on MainThread (was on ${Thread.currentThread().name})"
            observer?.onError(IllegalStateException(message))
            return
        }
        val listener = Listener(map, observer)
        observer?.onSubscribe(listener)
        map.setOnMapClickListener(listener)
    }

    class Listener(val map: GoogleMap,
                   val observer: Observer<in LatLng>?
    ) : MainThreadDisposable(), GoogleMap.OnMapClickListener {
        override fun onMapClick(position: LatLng) {
            if (!isDisposed) {
                observer?.onNext(position)
            }
        }

        override fun onDispose() {
            map.setOnCameraMoveListener(null)
        }

    }

}