package com.jonasgerdes.stoppelmap.map

import com.jonasgerdes.stoppelmap.map.usecase.GetFullStallsBySlugUseCase
import com.jonasgerdes.stoppelmap.map.usecase.SearchForStallsUseCase
import com.jonasgerdes.stoppelmap.map.view.MapViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mapModule = module {

    single { SearchForStallsUseCase(stallRepository = get()) }
    single { GetFullStallsBySlugUseCase(stallRepository = get()) }

    viewModel { MapViewModel(searchForStalls = get(), getStallsBySlug = get()) }
}