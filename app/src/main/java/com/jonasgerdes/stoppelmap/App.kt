package com.jonasgerdes.stoppelmap

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.jonasgerdes.stoppelmap.model.map.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.model.news.DynamicDatabase
import com.jonasgerdes.stoppelmap.util.versioning.VersionProviderImpl
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
        AndroidThreeTen.init(this)
        StoppelMapDatabase.init(this)
        DynamicDatabase.init(this)
        VersionProviderImpl.init(this)
    }

    private fun initMapbox() {
        Mapbox.getInstance(applicationContext, "pk.NOT_USED")
        TelemetryEnabler.updateTelemetryState(TelemetryEnabler.State.DISABLED)
    }

}