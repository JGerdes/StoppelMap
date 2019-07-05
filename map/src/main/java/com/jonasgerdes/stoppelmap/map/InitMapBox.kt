package com.jonasgerdes.stoppelmap.map

import android.content.Context
import com.mapbox.mapboxsdk.Mapbox

fun initMapBox(context: Context) {
    Mapbox.getInstance(context, null);
}