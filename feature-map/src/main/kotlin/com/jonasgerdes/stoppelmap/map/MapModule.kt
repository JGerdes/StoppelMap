package com.jonasgerdes.stoppelmap.map


import com.jonasgerdes.stoppelmap.data.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.map.repository.StallRepository
import com.jonasgerdes.stoppelmap.map.ui.MapViewModel
import com.jonasgerdes.stoppelmap.map.usecase.InitializeMapBoxUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mapModule = module {

    single { StallRepository(stallQueries = get<StoppelMapDatabase>().stallQueries) }

    single { InitializeMapBoxUseCase() }

    viewModel {
        MapViewModel(stallRepository = get())
    }
}
