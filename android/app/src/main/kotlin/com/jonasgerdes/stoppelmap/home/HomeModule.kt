package com.jonasgerdes.stoppelmap.home

import com.jonasgerdes.stoppelmap.home.ui.HomeViewModel
import com.jonasgerdes.stoppelmap.home.usecase.GetRemoteMessagesUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {

    factory { GetRemoteMessagesUseCase(appConfigRepository = get(), appInfo = get()) }

    viewModel {
        HomeViewModel(
            getAppUpdateState = get(),
            getRemoteMessages = get(),
            completeAppUpdate = get(),
            getOpeningCountDownState = get(),
            shouldShowCountdownWidgetSuggestion = get(),
            getNextOfficialEvent = get(),
            getNextBookmarkedEvent = get(),
        )
    }
}
