package com.jonasgerdes.stoppelmap.transportation.model

data class RouteSummary(
    val slug: String,
    val name: String,
)

data class DetailedRoute(
    val name: String,
    val operatorName: String,
    val operatorSlug: String,
    val arrivalStationSlug: String,
    val additionalInfo: String?,
    val ticketWebsites: List<TicketWebsite>,
)

data class TicketWebsite(
    val url: String,
    val label: String?,
)

data class StationSummary(
    val slug: String,
    val name: String,
    val annotateAsNew: Boolean,
)