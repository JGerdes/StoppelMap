package com.jonasgerdes.stoppelmap

import android.app.Application
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.jakewharton.threetenabp.AndroidThreeTen
import com.jonasgerdes.stoppelmap.core.di.Injector
import com.jonasgerdes.stoppelmap.data.RoomStoppelmapDatabase
import com.jonasgerdes.stoppelmap.di.*
import com.jonasgerdes.stoppelmap.map.initMapBox
import com.jonasgerdes.stoppelmap.news.fcm.subscribeToNewsMessages

class App : Application() {

    private lateinit var appComponent: AppComponent

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

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result?.token
                    Log.d("Firebase", "getInstanceId succeeded, token: $token")
                } else {
                    Log.w("Firebase", "getInstanceId failed", task.exception)
                }
            }
    }

    override fun getSystemService(name: String): Any? = when (name) {
        Injector.SERVICE_NAME -> InjectorImpl(appComponent)
        else -> super.getSystemService(name)
    }

    val fragmentFactory get() = appComponent.fragmentFactory()
}