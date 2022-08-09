package com.jonasgerdes.stoppelmap.preperation.entity

import java.time.OffsetDateTime

data class JsonLocation(val lat: Double, val lng: Double)
data class JsonPrice(val type: String, val price: Int)
data class JsonDeparture(val time: OffsetDateTime)
data class JsonDay(val day: Int, val departures: List<JsonDeparture>)
class ParsedStation(val name: String) {
    var uuid: String? = null
    var comment: String? = null
    var geoLocation: JsonLocation? = null
    var prices: List<JsonPrice>? = null
    var days: List<JsonDay>? = null
}

data class JsonRoute(
    val name: String,
    val uuid: String?,
    val days: List<JsonDay>? = null,
    val stations: List<ParsedStation>,
    val returnStation: ParsedStation
)

data class ScheduleFile(
    val routes: List<JsonRoute>
)