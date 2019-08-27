package com.jonasgerdes.stoppelmap

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.jonasgerdes.stoppelmap.data.StoppelmapDatabase
import com.jonasgerdes.stoppelmap.data.dataModule
import com.jonasgerdes.stoppelmap.events.eventsModule
import com.jonasgerdes.stoppelmap.home.homeModule
import com.jonasgerdes.stoppelmap.map.initMapBox
import com.jonasgerdes.stoppelmap.map.mapModule
import com.jonasgerdes.stoppelmap.news.fcm.subscribeToNewsMessages
import com.jonasgerdes.stoppelmap.news.newsModule
import com.jonasgerdes.stoppelmap.transport.transportModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            androidLogger()

            modules(
                appModule,
                dataModule,
                homeModule,
                mapModule,
                eventsModule,
                transportModule,
                newsModule
            )
        }

        AndroidThreeTen.init(this)

        StoppelmapDatabase.init(this)
        initMapBox(this)
        subscribeToNewsMessages(this)
    }
}