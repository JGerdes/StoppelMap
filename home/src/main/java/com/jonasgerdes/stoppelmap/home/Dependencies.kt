package com.jonasgerdes.stoppelmap.home

import com.jonasgerdes.stoppelmap.home.usescase.GetCountdownUseCase
import com.jonasgerdes.stoppelmap.home.usescase.GetStoppelmarktDatesUseCase
import com.jonasgerdes.stoppelmap.home.view.HomeViewModel
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


@FlowPreview
val homeModule = module {

    single { GetStoppelmarktDatesUseCase() }
    single { GetCountdownUseCase(currentDateTime = get(), globalInfoProvider = get()) }

    viewModel { HomeViewModel(getCountdown = get()) }
}