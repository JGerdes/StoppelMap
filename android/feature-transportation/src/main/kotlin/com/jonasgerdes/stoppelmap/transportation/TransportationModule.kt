package com.jonasgerdes.stoppelmap.transportation

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.jonasgerdes.stoppelmap.data.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.transportation.data.BusRoutesRepository
import com.jonasgerdes.stoppelmap.transportation.data.DatabaseTransportDataSource
import com.jonasgerdes.stoppelmap.transportation.data.TaxiServiceRepository
import com.jonasgerdes.stoppelmap.transportation.data.TrainRoutesRepository
import com.jonasgerdes.stoppelmap.transportation.data.TransportDataSource
import com.jonasgerdes.stoppelmap.transportation.data.TransportationUserDataRepository
import com.jonasgerdes.stoppelmap.transportation.ui.overview.TransportationOverviewViewModel
import com.jonasgerdes.stoppelmap.transportation.ui.route.RouteViewModel
import com.jonasgerdes.stoppelmap.transportation.ui.station.StationViewModel
import com.jonasgerdes.stoppelmap.transportation.usecase.CreateTimetableUseCase
import com.jonasgerdes.stoppelmap.transportation.usecase.GetNextDeparturesUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "transportationUserdata")

val transportationModule = module {

    single<TransportDataSource> {
        val database = get<StoppelMapDatabase>()
        DatabaseTransportDataSource(
            routeQueries = database.routeQueries,
            stationQueries = database.stationQueries,
            priceQueries = database.priceQueries,
            departureDayQueries = database.departure_dayQueries,
            departureQueries = database.departureQueries,
        )
    }

    single { BusRoutesRepository(transportDataSource = get()) }
    single { TrainRoutesRepository(transportDataSource = get()) }
    single { TaxiServiceRepository() }

    single {
        TransportationUserDataRepository(dataStore = get<Context>().dataStore)
    }

    factory { CreateTimetableUseCase() }
    factory { GetNextDeparturesUseCase(clockProvider = get()) }

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
