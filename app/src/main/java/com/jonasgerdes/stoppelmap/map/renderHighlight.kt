package com.jonasgerdes.stoppelmap.map

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import com.jonasgerdes.stoppelmap.domain.MainState
import com.jonasgerdes.stoppelmap.util.mapbox.toCenter
import com.mapbox.mapboxsdk.maps.MapboxMap
import io.reactivex.Observable

@SuppressLint("CheckResult")
fun renderHighlight(activity: Activity?, view: View?, map: MapboxMap,
                    state: Observable<MainState.MapState>) {
    state.map { it.highlight }
            .distinctUntilChanged()
            .subscribe {
                when (it) {
                    is MapHighlight.Center -> {
                        map.animateCamera(toCenter(it.latitude, it.longitude))
                    }
                }
            }
}