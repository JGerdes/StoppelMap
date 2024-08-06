package com.jonasgerdes.stoppelmap.transportation.model

import kotlinx.datetime.LocalDate

data class DepartureDay(
    val slug: String,
    val day: LocalDate,
    val departures: List<Departure>
)