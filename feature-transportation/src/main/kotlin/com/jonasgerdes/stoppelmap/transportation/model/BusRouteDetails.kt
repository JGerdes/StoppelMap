package com.jonasgerdes.stoppelmap.transportation.model

import kotlinx.datetime.LocalDateTime

data class BusRouteDetails(
    val routeId: String,
    val title: String,
    val stations: List<Station>
) {

    sealed class Station {
        abstract val id: String
        abstract val title: String

        data class Stop(
            override val id: String,
            override val title: String,
            val nextDepartures: List<DepartureTime>
        ) : Station()

        data class Destination(
            override val id: String,
            override val title: String
        ) : Station()
    }


    sealed class DepartureTime {
        object Immediately : DepartureTime()
        data class Relative(val minutes: Int) : DepartureTime()
        data class Absolut(val time: LocalDateTime) : DepartureTime()
    }

}
