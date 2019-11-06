package com.jonasgerdes.stoppelmap

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.jonasgerdes.stoppelmap.data.RoomStoppelmapDatabase
import com.jonasgerdes.stoppelmap.di.AppComponent
import com.jonasgerdes.stoppelmap.di.ContextModule
import com.jonasgerdes.stoppelmap.di.DaggerAppComponent
import com.jonasgerdes.stoppelmap.di.StoppelmapModule
import com.jonasgerdes.stoppelmap.map.initMapBox
import com.jonasgerdes.stoppelmap.news.fcm.subscribeToNewsMessages

class App : Application() {

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .contextModule(ContextModule(this))
            .stoppelmapModule(StoppelmapModule())
            .build()

        AndroidThreeTen.init(this)

        RoomStoppelmapDatabase.init(this)
        initMapBox(this)
        subscribeToNewsMessages(this)

    }

}