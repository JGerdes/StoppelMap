package com.jonasgerdes.stoppelmap.transportation.ui

import com.jonasgerdes.stoppelmap.base.contract.ClockProvider
import com.jonasgerdes.stoppelmap.transportation.data.BusRoutesRepository
import com.jonasgerdes.stoppelmap.transportation.data.TaxiServiceRepository
import com.jonasgerdes.stoppelmap.transportation.data.TrainRoutesRepository
import com.jonasgerdes.stoppelmap.transportation.data.TransportationUserDataRepository
import com.jonasgerdes.stoppelmap.transportation.usecase.CreateTimetableUseCase
import com.jonasgerdes.stoppelmap.transportation.usecase.GetNextDeparturesUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TransportationDependencies : KoinComponent {
    val busRoutesRepository: BusRoutesRepository by inject()
    val trainRoutesRepository: TrainRoutesRepository by inject()
    val taxiServiceRepository: TaxiServiceRepository by inject()
    val transportationUserDataRepository: TransportationUserDataRepository by inject()
    val getNextDepartures: GetNextDeparturesUseCase by inject()
    val clockProvider: ClockProvider by inject()
    val createTimetable: CreateTimetableUseCase by inject()
}