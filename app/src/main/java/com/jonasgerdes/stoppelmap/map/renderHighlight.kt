package com.jonasgerdes.stoppelmap.map

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.domain.MainState
import com.jonasgerdes.stoppelmap.model.map.StallCollectionCard
import com.jonasgerdes.stoppelmap.model.map.toBounds
import com.jonasgerdes.stoppelmap.util.dp
import com.jonasgerdes.stoppelmap.util.getColorForStallType
import com.jonasgerdes.stoppelmap.util.getMapBoxIcon
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
                map.removeMarkers()
            }
            is MapHighlight.Area -> {
                map.animateCamera(toBounds(highlight.bounds,
                        left = 64.dp,
                        top = 128.dp,
                        right = 64.dp,
                        bottom = if (it.cards.isEmpty()) 64.dp else 256.dp
                ), if (it.cards.isEmpty()) 300 else 600)
                map.removeMarkers()
            }
            is MapHighlight.MultiplePoints -> {
                map.animateCamera(toBounds(highlight.points.toBounds(),
                        left = 32.dp,
                        top = 112.dp,
                        right = 32.dp,
                        bottom = if (it.cards.isEmpty()) 32.dp else 208.dp))

                activity?.let { context ->
                    val type = (it.cards.find { it is StallCollectionCard } as? StallCollectionCard)?.type
                    val color = context.getColorForStallType(type, R.color.colorPrimary)
                    val icon = context.getMapBoxIcon(
                            R.drawable.ic_map_marker_24dp,
                            color,
                            32.dp
                    )
                    map.setMarkers(highlight.points)
                }
            }
            MapHighlight.None -> {
                map.removeMarkers()
            }
        }
    }

    state.map { it.cards }
            .distinctUntilChanged()
            .subscribe {
                cardAdapter.submitList(it)
            }
}
