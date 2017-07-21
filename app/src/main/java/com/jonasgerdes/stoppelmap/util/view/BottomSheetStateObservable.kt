package com.jonasgerdes.stoppelmap.util.map

import android.os.Looper
import android.view.View
import co.com.parsoniisolutions.custombottomsheetbehavior.lib.BottomSheetBehaviorGoogleMapsLike
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 21.07.2017
 */

class BottomSheetStateObservable(val behavior: BottomSheetBehaviorGoogleMapsLike<in View>)
    : Observable<Int>() {
    override fun subscribeActual(observer: Observer<in Int>?) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            val message = "Not on MainThread (was on ${Thread.currentThread().name})"
            observer?.onError(IllegalStateException(message))
            return
        }
        val listener = Listener(behavior, observer)
        observer?.onSubscribe(listener)
        behavior.addBottomSheetCallback(listener)
    }

    class Listener(val behavior: BottomSheetBehaviorGoogleMapsLike<in View>,
                   val observer: Observer<in Int>?
    ) : MainThreadDisposable(), BottomSheetBehaviorGoogleMapsLike.BottomSheetCallback {
        override fun onStateChanged(bottomSheet: View, state: Int) {
            if (!isDisposed) {
                observer?.onNext(state)
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            //ignored
        }

        override fun onDispose() {
            behavior.removeBottomSheetCallback(this)
        }

    }
}

fun BottomSheetBehaviorGoogleMapsLike<in View>.stateChanges(): Observable<Int> {
    return BottomSheetStateObservable(this)
}