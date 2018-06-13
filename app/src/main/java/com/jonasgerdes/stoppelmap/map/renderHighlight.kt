package com.jonasgerdes.stoppelmap.map

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import com.jonasgerdes.stoppelmap.domain.MainState
import com.jonasgerdes.stoppelmap.util.dp
import com.jonasgerdes.stoppelmap.util.mapbox.toBounds
import com.jonasgerdes.stoppelmap.util.mapbox.toCenter
import com.mapbox.mapboxsdk.maps.MapboxMap
import io.reactivex.Observable

@SuppressLint("CheckResult")
fun renderHighlight(activity: Activity?, view: View?, map: MapboxMap,
                    cardAdapter: StallCardAdapter,
                    state: Observable<MainState.MapState>) {
    state.subscribe {
        val highlight = it.highlight
        when (highlight) {
            is MapHighlight.Center -> {
                map.animateCamera(toCenter(highlight.latitude, highlight.longitude))
            }
            is MapHighlight.Area -> {
                map.animateCamera(toBounds(highlight.bounds,
                        left = 64.dp,
                        top = 128.dp,
                        right = 64.dp,
                        bottom = if (it.cards.isEmpty()) 64.dp else 256.dp
                ), if (it.cards.isEmpty()) 300 else 600)
            }
        }
    }

    state.map { it.cards }
            .distinctUntilChanged()
            .subscribe {
                cardAdapter.submitList(it)
            }
}