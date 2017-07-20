package com.jonasgerdes.stoppelmap.util.map

import android.os.Looper
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 20.07.2017
 */

class MapMarkerClickObservable(val map: GoogleMap) : Observable<Marker>() {
    override fun subscribeActual(observer: Observer<in Marker>?) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            val message = "Not on MainThread (was on ${Thread.currentThread().name})"
            observer?.onError(IllegalStateException(message))
            return
        }
        val listener = Listener(map, observer)
        observer?.onSubscribe(listener)
        map.setOnMarkerClickListener(listener)
    }

    class Listener(val map: GoogleMap,
                   val observer: Observer<in Marker>?
    ) : MainThreadDisposable(), GoogleMap.OnMarkerClickListener {
        override fun onMarkerClick(marker: Marker): Boolean {
            if (!isDisposed) {
                observer?.onNext(marker)
            }
            return true
        }

        override fun onDispose() {
            map.setOnMarkerClickListener(null)
        }

    }

}