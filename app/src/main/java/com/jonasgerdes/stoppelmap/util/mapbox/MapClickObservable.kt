package com.jonasgerdes.stoppelmap.util.map

import android.os.Looper
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

class MapClickObservable(private val map: MapboxMap) : Observable<LatLng>() {
    override fun subscribeActual(observer: Observer<in LatLng>?) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            val message = "Not on MainThread (was on ${Thread.currentThread().name})"
            observer?.onError(IllegalStateException(message))
            return
        }
        val listener = Listener(map, observer)
        observer?.onSubscribe(listener)
        map.addOnMapClickListener(listener)
    }

    class Listener(private val map: MapboxMap,
                   private val observer: Observer<in LatLng>?
    ) : MainThreadDisposable(), MapboxMap.OnMapClickListener {
        override fun onMapClick(position: LatLng) {
            if (!isDisposed) {
                observer?.onNext(position)
            }
        }

        override fun onDispose() {
            map.removeOnMapClickListener(this)
        }

    }

}