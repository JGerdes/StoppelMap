package com.jonasgerdes.stoppelmap.transportation.data

import com.jonasgerdes.stoppelmap.transportation.model.BusRouteSummary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class BusRoutesRepository {

    fun getAllRoutes(): Flow<List<BusRouteSummary>> = flowOf(
        listOf(
            BusRouteSummary(
                id = "2022-bus-vechta-stadt",
                title = "Vechta Stadt",
                viaStations = listOf(
                    "Sgundek",
                    "Burgstraße, Altes Finanzamt",
                    "Botenkamp/Markusstraße"
                )
            )
        )
    )

}
