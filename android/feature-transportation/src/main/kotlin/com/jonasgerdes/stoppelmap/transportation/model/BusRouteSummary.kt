package com.jonasgerdes.stoppelmap.transportation.model

data class BusRouteSummary(
    val id: String,
    val title: String,
    val viaStations: List<String>
)
