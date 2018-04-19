package com.jonasgerdes.stoppelmap

import android.app.Application
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.services.android.telemetry.MapboxTelemetry

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 16.04.2018
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initMapbox()
    }

    private fun initMapbox() {
        Mapbox.getInstance(applicationContext, "pk.NOT_USED")
        MapboxTelemetry.getInstance().isTelemetryEnabled = false
    }

}