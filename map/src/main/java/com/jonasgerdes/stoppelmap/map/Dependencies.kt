package com.jonasgerdes.stoppelmap.map

import com.jonasgerdes.stoppelmap.map.usecase.*
import com.jonasgerdes.stoppelmap.map.view.MapViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mapModule = module {

    single { SearchForStallsUseCase(stallRepository = get()) }
    single { GetFullStallsBySlugUseCase(stallRepository = get()) }
    single { CreateSingleStallHighlightUseCase(stallRepository = get(), getFullStallsBySlug = get()) }
    single { CreateTypeHighlightsUseCase(stallRepository = get(), getFullStallsBySlug = get()) }
    single { CreateItemHighlightsUseCase(stallRepository = get(), getFullStallsBySlug = get()) }
    single { GetUserLocationUseCase(locationProvider = get()) }
    single { IsUserInAreaUseCase(globalInfoProvider = get()) }

    viewModel {
        MapViewModel(
            searchForStalls = get(),
            getStallsBySlug = get(),
            createSingleStallHighlight = get(),
            createItemHighlights = get(),
            createTypeHighlights = get(),
            getUserLocation = get(),
            isUserInArea = get()
        )
    }
}