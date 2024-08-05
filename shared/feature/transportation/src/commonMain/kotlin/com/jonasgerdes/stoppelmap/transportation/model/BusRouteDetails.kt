package com.jonasgerdes.stoppelmap.transportation.model

data class BusRouteDetails(
    val name: String,
    val additionalInfo: String? = null,
    val operator: Operator,
    val stations: List<Station>,
    val destination: Destination
) {
    data class Operator(
        val slug: String,
        val name: String,
    )

    data class Station(
        val slug: String,
        val name: String,
        val nextDepartures: List<DepartureTime>,
        val annotateAsNew: Boolean = false,
    )

    data class Destination(
        val slug: String,
        val name: String?,
    )
}
