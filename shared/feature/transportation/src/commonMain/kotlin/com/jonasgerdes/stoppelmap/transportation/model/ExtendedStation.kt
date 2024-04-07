package com.jonasgerdes.stoppelmap.transportation.model

import com.jonasgerdes.stoppelmap.data.model.database.RouteType

data class ExtendedStation(
    val station: Station,
    val routeTitle: String,
    val routeType: RouteType
)
