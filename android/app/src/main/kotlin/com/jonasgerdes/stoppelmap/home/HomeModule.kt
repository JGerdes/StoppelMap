package com.jonasgerdes.stoppelmap.home

import com.jonasgerdes.stoppelmap.home.ui.HomeViewModel
import com.jonasgerdes.stoppelmap.home.usecase.GetRemoteMessagesUseCase
import com.jonasgerdes.stoppelmap.usecase.IsCurrentYearsSeasonJustOverUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {

    factory { IsCurrentYearsSeasonJustOverUseCase(seasonProvider = get(), clockProvider = get()) }

    factory { GetRemoteMessagesUseCase(appConfigRepository = get(), appInfo = get()) }

    viewModel {
        HomeViewModel(
            getAppUpdateState = get(),
            getRemoteMessages = get(),
            completeAppUpdate = get(),
            getOpeningCountDown = get(),
            shouldShowCountdownWidgetSuggestion = get(),
            getNextOfficialEvent = get(),
            getNextBookmarkedEvent = get(),
            isCurrentYearsSeasonJustOver = get()
        )
    }
}
