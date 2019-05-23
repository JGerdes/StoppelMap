package com.jonasgerdes.stoppelmap

import android.app.Application
import com.jonasgerdes.stoppelmap.core.routing.Route
import com.jonasgerdes.stoppelmap.core.routing.Router
import com.jonasgerdes.stoppelmap.fcm.subscribeToNewsMessages

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        subscribeToNewsMessages()
    }
}