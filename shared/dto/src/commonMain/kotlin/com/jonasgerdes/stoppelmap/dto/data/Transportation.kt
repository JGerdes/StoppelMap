package com.jonasgerdes.stoppelmap.dto.data

import com.jonasgerdes.stoppelmap.dto.Localized
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Transportation(
    val busRoutes: List<Route>,
    val trainRoutes: List<Route>,
    val taxiServices: List<ServiceSlug>,
    val isWorkInProgress: Boolean,
)

typealias RouteSlug = String

@Serializable
data class Route(
    val slug: RouteSlug,
    val name: String,
    val operator: OperatorSlug? = null,
    val additionalInfo: Localized<String>? = null,
    val ticketWebsites: List<Website> = emptyList(),
    val stations: List<Station>,
    val arrivalStationSlug: MapEntitySlug,
)

@Serializable
data class Station(
    val slug: String,
    val name: String,
    val location: Location? = null,
    val isNew: Boolean,
    val additionalInfo: Localized<String>? = null,
    val prices: List<Fee> = emptyList(),
    val ticketWebsites: List<Website> = emptyList(),
    val outward: List<DepartureDay>,
    val returns: List<DepartureDay>,
)


@Serializable
data class DepartureDay(
    val day: LocalDate,
    val departures: List<Departure>,
    val laterDepartureOnDemand: Boolean,
)

@Serializable
data class Departure(
    val time: LocalDateTime,
    val arrival: LocalDateTime?,
    val annotation: Localized<String>? = null
)
