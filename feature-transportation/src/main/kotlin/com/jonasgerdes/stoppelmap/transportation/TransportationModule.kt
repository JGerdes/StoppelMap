package com.jonasgerdes.stoppelmap.transportation

import com.jonasgerdes.stoppelmap.transportation.data.BusRoutesRepository
import com.jonasgerdes.stoppelmap.transportation.data.StaticTransportDataSource
import com.jonasgerdes.stoppelmap.transportation.data.TransportDataSource
import com.jonasgerdes.stoppelmap.transportation.ui.overview.TransportationOverviewViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val transportationModule = module {

    single<TransportDataSource> { StaticTransportDataSource() }

    single { BusRoutesRepository(transportDataSource = get()) }

    viewModel {
        TransportationOverviewViewModel(
            busRoutesRepository = get()
        )
    }
}
