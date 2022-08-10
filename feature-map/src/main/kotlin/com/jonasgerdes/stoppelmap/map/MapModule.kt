package com.jonasgerdes.stoppelmap.map


import com.jonasgerdes.stoppelmap.data.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.map.repository.StallRepository
import com.jonasgerdes.stoppelmap.map.repository.TypeRepository
import com.jonasgerdes.stoppelmap.map.ui.MapViewModel
import com.jonasgerdes.stoppelmap.map.usecase.InitializeMapBoxUseCase
import com.jonasgerdes.stoppelmap.map.usecase.SearchStallsUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mapModule = module {

    single { StallRepository(stallQueries = get<StoppelMapDatabase>().stallQueries) }
    single { TypeRepository(typeQueries = get<StoppelMapDatabase>().sub_typesQueries) }

    single { InitializeMapBoxUseCase() }

    factory { SearchStallsUseCase(stallRepository = get(), typeRepository = get()) }

    viewModel {
        MapViewModel(stallRepository = get(), searchStalls = get())
    }
}
