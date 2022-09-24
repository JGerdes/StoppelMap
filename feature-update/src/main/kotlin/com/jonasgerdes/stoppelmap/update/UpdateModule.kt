package com.jonasgerdes.stoppelmap.update

import android.content.Context
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.jonasgerdes.stoppelmap.update.usecase.GetAppUpdateDownloadStateUseCase
import org.koin.dsl.module

val updateModule = module {

    single {
        val context: Context = get()
        AppUpdateManagerFactory.create(context)
    }

    factory {
        GetAppUpdateDownloadStateUseCase(appUpdateManager = get())
    }

}
