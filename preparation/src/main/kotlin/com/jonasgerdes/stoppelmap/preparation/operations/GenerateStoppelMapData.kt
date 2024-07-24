package com.jonasgerdes.stoppelmap.preparation.operations

import com.jonasgerdes.stoppelmap.data.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.dto.data.Definitions
import com.jonasgerdes.stoppelmap.dto.data.Map
import com.jonasgerdes.stoppelmap.dto.data.Schedule
import com.jonasgerdes.stoppelmap.dto.data.StoppelMapData
import com.jonasgerdes.stoppelmap.dto.data.Transportation
import com.jonasgerdes.stoppelmap.preparation.Settings
import com.jonasgerdes.stoppelmap.preparation.transportation.TransportOperators
import com.jonasgerdes.stoppelmap.preparation.transportation.generateBusRoutes
import com.jonasgerdes.stoppelmap.preparation.transportation.generateTrainRoutes
import com.jonasgerdes.stoppelmap.preparation.transportation.taxi.generateTaxiServices
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GenerateStoppelMapData : KoinComponent {

    private val settings: Settings by inject()
    private val database: StoppelMapDatabase by inject()

    operator fun invoke(): StoppelMapData {

        val taxiServices = generateTaxiServices()

        return StoppelMapData(
            version = 0, // TODO: generate version
            schemaVersion = 0, // TODO: generate version
            seasonYear = 2024, // TODO: put to settings?
            definitions = Definitions(
                tags = listOf(),
                subTypes = listOf(),
                products = listOf(),
                services = taxiServices,
                persons = listOf(),
                operators = TransportOperators.all()
            ),
            map = Map(
                entities = listOf(),
                isWorkInProgress = true
            ),
            schedule = Schedule(
                events = listOf(),
                isWorkInProgress = true

            ),
            transportation = Transportation(
                busRoutes = generateBusRoutes(),
                trainRoutes = generateTrainRoutes(),
                taxiServices = taxiServices.map { it.slug },
                isWorkInProgress = true,
            ),
            note = null,
        )
    }
}