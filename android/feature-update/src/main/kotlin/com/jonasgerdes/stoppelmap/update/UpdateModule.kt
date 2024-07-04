package com.jonasgerdes.stoppelmap.update

import android.content.Context
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.jonasgerdes.stoppelmap.update.ui.AppUpdateViewModel
import com.jonasgerdes.stoppelmap.update.usecase.CompleteAppUpdateUseCase
import com.jonasgerdes.stoppelmap.update.usecase.GetAppUpdateStateUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val updateModule = module {

    single {
        val context: Context = get()
        AppUpdateManagerFactory.create(context)
    }

    factory {
        GetAppUpdateStateUseCase(appUpdateManager = get())
    }

    factory {
        CompleteAppUpdateUseCase(appUpdateManager = get())
    }

    viewModel {
        AppUpdateViewModel(
            getAppUpdateState = get(),
            completeAppUpdate = get(),
        )
    }

}
