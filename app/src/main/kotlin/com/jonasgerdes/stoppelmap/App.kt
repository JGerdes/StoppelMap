package com.jonasgerdes.stoppelmap

import android.app.Application
import android.appwidget.AppWidgetManager
import com.jonasgerdes.stoppelmap.countdown.countdownModule
import com.jonasgerdes.stoppelmap.countdown.widget.heart.GingerbreadHeartWidgetProvider
import com.jonasgerdes.stoppelmap.countdown.widget.skyline.SkylineWidgetProvider
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
        
        val appWidgetManager = AppWidgetManager.getInstance(this)
        GingerbreadHeartWidgetProvider().updateAllWidgets(
            context = this,
            appWidgetManager = appWidgetManager
        )
        SkylineWidgetProvider().updateAllWidgets(
            context = this,
            appWidgetManager = appWidgetManager
        )
    }
}
