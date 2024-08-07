package com.jonasgerdes.stoppelmap.transportation.model

data class BusRouteDetails(
    val name: String,
    val additionalInfo: String? = null,
    val operator: Operator,
    val stations: List<Station>,
    val destination: Destination,
    val ticketWebsites: List<TicketWebsite>
) {
    data class Operator(
        val slug: String,
        val name: String,
    )

    data class Station(
        val slug: String,
        val name: String,
        val nextDepartures: NextDepartures,
        val annotateAsNew: Boolean = false,
    ) {
        sealed interface NextDepartures {
            data object Loading : NextDepartures
            data class Loaded(val departures: List<DepartureTime>) : NextDepartures
        }
    }

    data class Destination(
        val slug: String,
        val name: String?,
    )
}
