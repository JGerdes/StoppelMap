package com.jonasgerdes.stoppelmap.transport

import com.jonasgerdes.stoppelmap.transport.usecase.GetFullRouteUseCase
import com.jonasgerdes.stoppelmap.transport.usecase.GetFullStationUseCase
import com.jonasgerdes.stoppelmap.transport.usecase.GetRoutesUseCase
import com.jonasgerdes.stoppelmap.transport.view.TransportViewModel
import com.jonasgerdes.stoppelmap.transport.view.route.RouteDetailViewModel
import com.jonasgerdes.stoppelmap.transport.view.station.StationDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val transportModule = module {
    single { GetRoutesUseCase(routeRepository = get()) }
    single { GetFullRouteUseCase(routeRepository = get(), getCurrentTime = get()) }
    single { GetFullStationUseCase(routeRepository = get(), getCurrentTime = get()) }

    viewModel {
        TransportViewModel(
            getRoutes = get()
        )
    }

    viewModel { (routeSlug: String) ->
        RouteDetailViewModel(
            routeSlug = routeSlug,
            getFullRoute = get()
        )
    }

    viewModel { (stationSlug: String) ->
        StationDetailViewModel(
            stationSlug = stationSlug,
            getFullStation = get()
        )
    }
}