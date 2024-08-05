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
    val additionalInfo: String?
)

data class StationSummary(
    val slug: String,
    val name: String,
    val annotateAsNew: Boolean,
)