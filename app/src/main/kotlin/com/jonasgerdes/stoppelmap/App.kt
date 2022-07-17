package com.jonasgerdes.stoppelmap

import android.app.Application
import com.jonasgerdes.stoppelmap.countdown.countdownModule
import com.jonasgerdes.stoppelmap.home.homeModule
import org.koin.android.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@App)

            modules(
                homeModule,
                countdownModule,
            )
        }
    }
}