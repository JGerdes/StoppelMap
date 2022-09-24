package com.jonasgerdes.stoppelmap.update

import android.content.Context
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.testing.FakeAppUpdateManager
import com.jonasgerdes.stoppelmap.update.usecase.GetAppUpdateStateUseCase
import org.koin.dsl.module

val updateModule = module {

    single<AppUpdateManager> {
        val context: Context = get()
        // AppUpdateManagerFactory.create(context)
        FakeAppUpdateManager(context).also {
            //it.setUpdateAvailable(55, AppUpdateType.FLEXIBLE)
        }
    }

    factory {
        GetAppUpdateStateUseCase(appUpdateManager = get())
    }

}
