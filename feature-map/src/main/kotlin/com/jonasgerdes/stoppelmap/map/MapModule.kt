package com.jonasgerdes.stoppelmap.map


import android.content.Context
import com.google.android.gms.location.LocationServices
import com.jonasgerdes.stoppelmap.data.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.map.repository.LocationRepository
import com.jonasgerdes.stoppelmap.map.repository.PermissionRepository
import com.jonasgerdes.stoppelmap.map.repository.StallRepository
import com.jonasgerdes.stoppelmap.map.repository.TypeRepository
import com.jonasgerdes.stoppelmap.map.ui.MapViewModel
import com.jonasgerdes.stoppelmap.map.usecase.InitializeMapBoxUseCase
import com.jonasgerdes.stoppelmap.map.usecase.IsLocationInAreaUseCase
import com.jonasgerdes.stoppelmap.map.usecase.SearchStallsUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mapModule = module {

    single { StallRepository(stallQueries = get<StoppelMapDatabase>().stallQueries) }
    single { TypeRepository(typeQueries = get<StoppelMapDatabase>().sub_typesQueries) }

    single { PermissionRepository(context = get()) }

    single { LocationServices.getFusedLocationProviderClient(get<Context>()) }
    single { LocationRepository(fusedLocationProviderClient = get(), permissionRepository = get()) }

    single { InitializeMapBoxUseCase() }

    factory { SearchStallsUseCase(stallRepository = get(), typeRepository = get()) }
    factory { IsLocationInAreaUseCase() }

    viewModel {
        MapViewModel(
            stallRepository = get(),
            searchStalls = get(),
            locationRepository = get(),
            isLocationInArea = get()
        )
    }
}
