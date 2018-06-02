package com.jonasgerdes.stoppelmap

import android.app.Application
import com.jonasgerdes.stoppelmap.model.map.StoppelMapDatabase
import com.mapbox.android.telemetry.TelemetryEnabler
import com.mapbox.mapboxsdk.Mapbox

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 16.04.2018
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initMapbox()
        StoppelMapDatabase.init(this)
    }

    private fun initMapbox() {
        Mapbox.getInstance(applicationContext, "pk.NOT_USED")
        TelemetryEnabler.updateTelemetryState(TelemetryEnabler.State.DISABLED)
    }

}