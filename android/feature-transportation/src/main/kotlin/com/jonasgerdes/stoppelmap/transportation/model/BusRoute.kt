package com.jonasgerdes.stoppelmap.transportation.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

data class BusRoute(
    val id: String,
    val title: String,
    val stations: List<Station>,
    val returnStations: List<Station>,
    val additionalInfo: String? = null
)

data class Station(
    val id: String,
    val title: String,
    val prices: List<Price>,
    val isDestination: Boolean = false,
    val annotateAsNew: Boolean = false,
    val departures: List<DepartureDay>
)

data class Price(
    val label: PriceLabel,
    val amountInCents: Int,
) {
    sealed class PriceLabel {
        object Adult : PriceLabel()
        data class Children(
            val minAge: Int?,
            val maxAge: Int?
        ) : PriceLabel()
    }
}

data class DepartureDay(
    val day: LocalDate,
    val departures: List<Departure>,
    val laterDeparturesOnDemand: Boolean = false
)

data class Departure(
    val time: LocalDateTime,
    val annotation: String? = null,
)
