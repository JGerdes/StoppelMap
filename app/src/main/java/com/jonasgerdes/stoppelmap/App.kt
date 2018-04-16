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
        Mapbox.getInstance(applicationContext, BuildConfig.API_KEY_MAPBOX)
        MapboxTelemetry.getInstance().isTelemetryEnabled = false
    }

}