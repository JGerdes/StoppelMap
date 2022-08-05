package com.jonasgerdes.stoppelmap.transportation.model

import kotlinx.datetime.LocalDateTime

data class BusRouteDetails(
    val routeId: String,
    val title: String,
    val additionalInfo: String? = null,
    val stations: List<Station>,
    val returnStations: List<ReturnStation>,
) {

    sealed class Station {
        abstract val id: String
        abstract val title: String

        data class Stop(
            override val id: String,
            override val title: String,
            val nextDepartures: List<DepartureTime>,
            val annotateAsNew: Boolean = false,
        ) : Station()

        data class Destination(
            override val id: String,
            override val title: String
        ) : Station()
    }

    data class ReturnStation(
        val id: String,
        val title: String,
    )


    sealed class DepartureTime {
        object Immediately : DepartureTime()
        data class Relative(val minutes: Int) : DepartureTime()
        data class Absolut(val time: LocalDateTime) : DepartureTime()
    }

}
