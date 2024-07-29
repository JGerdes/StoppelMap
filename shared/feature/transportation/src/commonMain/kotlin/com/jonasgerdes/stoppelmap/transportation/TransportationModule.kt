package com.jonasgerdes.stoppelmap.transportation

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.jonasgerdes.stoppelmap.base.contract.PreferencesPathFactory
import com.jonasgerdes.stoppelmap.base.model.VenueInformation
import com.jonasgerdes.stoppelmap.transportation.data.BusRoutesRepository
import com.jonasgerdes.stoppelmap.transportation.data.DummyTransportDataSource
import com.jonasgerdes.stoppelmap.transportation.data.TaxiServiceRepository
import com.jonasgerdes.stoppelmap.transportation.data.TrainRoutesRepository
import com.jonasgerdes.stoppelmap.transportation.data.TransportDataSource
import com.jonasgerdes.stoppelmap.transportation.data.TransportationUserDataRepository
import com.jonasgerdes.stoppelmap.transportation.usecase.CreateTimetableUseCase
import com.jonasgerdes.stoppelmap.transportation.usecase.GetNextDeparturesUseCase
import okio.Path.Companion.toPath
import org.koin.dsl.module

val transportationModule = module {

    single<TransportDataSource> {
        DummyTransportDataSource()
    }

    single { BusRoutesRepository(transportDataSource = get()) }
    single { TrainRoutesRepository(transportDataSource = get()) }
    single { TaxiServiceRepository() }

    single {
        TransportationUserDataRepository(
            dataStore = PreferenceDataStoreFactory.createWithPath(
                corruptionHandler = null,
                migrations = emptyList(),
                produceFile = {
                    get<PreferencesPathFactory>().create("transportationUserdata").toPath()
                },
            )
        )
    }

    factory { CreateTimetableUseCase() }
    factory {
        GetNextDeparturesUseCase(
            clockProvider = get(),
            timeZone = get<VenueInformation>().timeZone
        )
    }
}
