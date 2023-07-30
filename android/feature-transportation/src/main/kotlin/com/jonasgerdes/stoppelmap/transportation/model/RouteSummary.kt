package com.jonasgerdes.stoppelmap.transportation.model

data class RouteSummary(
    val id: String,
    val title: String,
    val viaStations: List<String>
)
