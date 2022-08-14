package com.jonasgerdes.stoppelmap.transportation

import com.jonasgerdes.stoppelmap.transportation.data.BusRoutesRepository
import com.jonasgerdes.stoppelmap.transportation.data.StaticTransportDataSource
import com.jonasgerdes.stoppelmap.transportation.data.TransportDataSource
import com.jonasgerdes.stoppelmap.transportation.ui.overview.TransportationOverviewViewModel
import com.jonasgerdes.stoppelmap.transportation.ui.route.RouteViewModel
import com.jonasgerdes.stoppelmap.transportation.ui.station.StationViewModel
import com.jonasgerdes.stoppelmap.transportation.usecase.CreateTimetableUseCase
import com.jonasgerdes.stoppelmap.transportation.usecase.GetNextDeparturesUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val transportationModule = module {

    single<TransportDataSource> { StaticTransportDataSource() }

    single { BusRoutesRepository(transportDataSource = get()) }

    factory { CreateTimetableUseCase() }
    factory { GetNextDeparturesUseCase() }

    viewModel {
        TransportationOverviewViewModel(
            busRoutesRepository = get()
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
        StationViewModel(stationId = get(), busRoutesRepository = get(), createTimetable = get())
    }
}
