package com.jonasgerdes.stoppelmap.home

import com.jonasgerdes.stoppelmap.home.usescase.GetCountdownUseCase
import com.jonasgerdes.stoppelmap.home.view.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val homeModule = module {

    single { GetCountdownUseCase() }

    viewModel { HomeViewModel(getCountdown = get()) }
}