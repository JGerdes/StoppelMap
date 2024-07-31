package com.jonasgerdes.stoppelmap

import android.app.Application
import android.appwidget.AppWidgetManager
import android.content.Context
import androidx.glance.appwidget.updateAll
import com.jonasgerdes.stoppelmap.countdown.androidCountdownModule
import com.jonasgerdes.stoppelmap.di.initKoin
import com.jonasgerdes.stoppelmap.home.androidHomeModule
import com.jonasgerdes.stoppelmap.map.mapModule
import com.jonasgerdes.stoppelmap.news.androidNewsModule
import com.jonasgerdes.stoppelmap.news.usecase.LoadLatestNewsUseCase
import com.jonasgerdes.stoppelmap.schedule.androidScheduleModule
import com.jonasgerdes.stoppelmap.settings.androidLicensesModule
import com.jonasgerdes.stoppelmap.settings.settingsModule
import com.jonasgerdes.stoppelmap.shared.dataupdate.usecase.UpdateDataUseCase
import com.jonasgerdes.stoppelmap.shared.dataupdate.usecase.UpdateRemoteAppConfigUseCase
import com.jonasgerdes.stoppelmap.transportation.androidTransportationModule
import com.jonasgerdes.stoppelmap.update.updateModule
import com.jonasgerdes.stoppelmap.widget.countdown.CountdownWidget
import com.jonasgerdes.stoppelmap.widget.heart.GingerbreadHeartWidgetProvider
import com.jonasgerdes.stoppelmap.widget.silhouette.SilhouetteWidgetProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.dsl.module
import timber.log.Timber

class App : Application() {

    val scope: CoroutineScope by inject()
    val updateRemoteAppConfig: UpdateRemoteAppConfigUseCase by inject()
    val updateData: UpdateDataUseCase by inject()
    val loadLatestNews: LoadLatestNewsUseCase by inject()

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        initKoin(
            listOf(
                module {
                    single<Context> { this@App }
                },
                appModule,
                dateTimeModule,
                androidLicensesModule,
                androidHomeModule,
                settingsModule,
                androidCountdownModule,
                mapModule,
                androidScheduleModule,
                androidTransportationModule,
                androidNewsModule,
                updateModule,
            )
        )

        val appWidgetManager = AppWidgetManager.getInstance(this)
        GingerbreadHeartWidgetProvider().updateAllWidgets(
            context = this,
            appWidgetManager = appWidgetManager
        )
        SilhouetteWidgetProvider().updateAllWidgets(
            context = this,
            appWidgetManager = appWidgetManager
        )
        scope.launch {
            CountdownWidget().updateAll(context = this@App)
        }

        scope.launch {
            updateData()
        }

        scope.launch {
            updateRemoteAppConfig()
            loadLatestNews()
        }
    }
}
