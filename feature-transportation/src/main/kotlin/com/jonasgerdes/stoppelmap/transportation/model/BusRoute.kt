package com.jonasgerdes.stoppelmap.transportation.data.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

data class BusRoute(
    val id: String,
    val title: String,
    val stations: List<Station>,
    val returnStations: List<Station>
)

data class Station(
    val id: String,
    val title: String,
    val prices: List<Price>,
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
    val departures: List<LocalDateTime>,
    val laterDeparturesOnDemand: Boolean = false
)
