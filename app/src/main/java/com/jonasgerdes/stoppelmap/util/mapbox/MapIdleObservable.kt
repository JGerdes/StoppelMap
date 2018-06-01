package com.jonasgerdes.stoppelmap.util.map

import android.os.Looper
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.maps.MapboxMap
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 18.06.2017
 */

class MapIdleObservable(val map: MapboxMap) : Observable<CameraPosition>() {
    override fun subscribeActual(observer: Observer<in CameraPosition>?) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            val message = "Not on MainThread (was on ${Thread.currentThread().name})"
            observer?.onError(IllegalStateException(message))
            return
        }
        val listener = Listener(map, observer)
        observer?.onSubscribe(listener)
        map.addOnCameraIdleListener(listener)
    }

    class Listener(val map: MapboxMap,
                   val observer: Observer<in CameraPosition>?
    ) : MainThreadDisposable(), MapboxMap.OnCameraIdleListener {
        override fun onCameraIdle() {
            if (!isDisposed) {
                observer?.onNext(map.cameraPosition)
            }
        }

        override fun onDispose() {
            map.removeOnCameraIdleListener(this)
        }

    }

}