package com.jonasgerdes.stoppelmap.transportation

import com.jonasgerdes.stoppelmap.transportation.ui.overview.TransportationOverviewViewModel
import com.jonasgerdes.stoppelmap.transportation.ui.route.RouteViewModel
import com.jonasgerdes.stoppelmap.transportation.ui.station.StationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val androidTransportationModule = module {

    viewModel {
        TransportationOverviewViewModel(
            busRoutesRepository = get(),
            trainRoutesRepository = get(),
            taxiServiceRepository = get(),
            transportationUserDataRepository = get(),
            getNextDepartures = get(),
        )
    }

    viewModel {
        RouteViewModel(
            routeId = get(),
            busRoutesRepository = get(),
            clockProvider = get(),
            getNextDepartures = get()
        )
    }

    viewModel {
        StationViewModel(
            stationId = get(),
            busRoutesRepository = get(),
            transportationUserDataRepository = get(),
            createTimetable = get()
        )
    }
}
