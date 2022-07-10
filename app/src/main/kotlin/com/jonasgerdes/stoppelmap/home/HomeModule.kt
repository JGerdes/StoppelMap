package com.jonasgerdes.stoppelmap.home

import com.jonasgerdes.stoppelmap.home.ui.HomeViewModel
import com.jonasgerdes.stoppelmap.home.usecase.GetOpeningCountDownUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {

    factory {
        GetOpeningCountDownUseCase()
    }

    viewModel {
        HomeViewModel(getOpeningCountDown = get())
    }
}
