package com.jonasgerdes.stoppelmap

import android.app.Application
import android.appwidget.AppWidgetManager
import com.jonasgerdes.stoppelmap.countdown.countdownModule
import com.jonasgerdes.stoppelmap.data.dataModule
import com.jonasgerdes.stoppelmap.dataupdate.dataUpdateModule
import com.jonasgerdes.stoppelmap.dataupdate.usecase.CopyAssetDataFilesUseCase
import com.jonasgerdes.stoppelmap.dataupdate.usecase.UpdateAppConfigAndDownloadFilesUseCase
import com.jonasgerdes.stoppelmap.home.homeModule
import com.jonasgerdes.stoppelmap.map.mapModule
import com.jonasgerdes.stoppelmap.map.usecase.InitializeMapBoxUseCase
import com.jonasgerdes.stoppelmap.news.newsModule
import com.jonasgerdes.stoppelmap.schedule.scheduleModule
import com.jonasgerdes.stoppelmap.settings.settingsModule
import com.jonasgerdes.stoppelmap.transportation.transportationModule
import com.jonasgerdes.stoppelmap.update.updateModule
import com.jonasgerdes.stoppelmap.widget.heart.GingerbreadHeartWidgetProvider
import com.jonasgerdes.stoppelmap.widget.silhouette.SilhouetteWidgetProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.android.BuildConfig
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class App : Application() {

    val initializeMapBox: InitializeMapBoxUseCase by inject()
    val copyAssetDatabase: CopyAssetDataFilesUseCase by inject()
    val updateAppConfigAndDownloadFiles: UpdateAppConfigAndDownloadFilesUseCase by inject()
    val scope: CoroutineScope by inject()

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@App)

            modules(
                appModule,
                dateTimeModule,
                dataUpdateModule,
                licenseModule,
                dataModule,
                homeModule,
                settingsModule,
                countdownModule,
                mapModule,
                scheduleModule,
                transportationModule,
                newsModule,
                updateModule,
            )
        }

        val appWidgetManager = AppWidgetManager.getInstance(this)
        GingerbreadHeartWidgetProvider().updateAllWidgets(
            context = this,
            appWidgetManager = appWidgetManager
        )
        SilhouetteWidgetProvider().updateAllWidgets(
            context = this,
            appWidgetManager = appWidgetManager
        )

        initializeMapBox(this)

        scope.launch {
            copyAssetDatabase()
            updateAppConfigAndDownloadFiles()
        }
    }
}
