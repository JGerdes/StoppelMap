package com.jonasgerdes.stoppelmap.home

import com.jonasgerdes.stoppelmap.home.ui.HomeViewModel
import com.jonasgerdes.stoppelmap.usecase.IsCurrentYearsSeasonJustOverUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {

    factory { IsCurrentYearsSeasonJustOverUseCase(seasonProvider = get(), clockProvider = get()) }

    viewModel {
        HomeViewModel(
            getOpeningCountDown = get(),
            shouldShowCountdownWidgetSuggestion = get(),
            getNextOfficialEvent = get(),
            isCurrentYearsSeasonJustOver = get()
        )
    }
}
