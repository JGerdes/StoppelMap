package com.jonasgerdes.stoppelmap.home

import com.jonasgerdes.stoppelmap.home.ui.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val androidHomeModule = module {

    viewModel {
        HomeViewModel(
            getRemoteMessages = get(),
            getOpeningCountDownState = get(),
            shouldShowCountdownWidgetSuggestion = get(),
            getNextOfficialEvent = get(),
            getNextBookmarkedEvent = get(),
        )
    }
}
