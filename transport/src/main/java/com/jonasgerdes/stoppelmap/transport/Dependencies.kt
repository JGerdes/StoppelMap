package com.jonasgerdes.stoppelmap.transport

import com.jonasgerdes.stoppelmap.transport.usecase.GetRoutesUseCase
import com.jonasgerdes.stoppelmap.transport.view.TransportViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val transportModule = module {
    single { GetRoutesUseCase(routeRepository = get()) }

    viewModel {
        TransportViewModel(
            getRoutes = get()
        )
    }
}