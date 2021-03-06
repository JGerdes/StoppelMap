package com.jonasgerdes.stoppelmap.model.entity

import java.util.*

data class JsonLocation(val lat: Double, val lng: Double)
data class JsonPrice(val type: String, val price: Int)
data class JsonDeparture(val time: Date)
data class JsonDay(val day: Int, val departures: List<JsonDeparture>)
data class JsonStation(
        val name: String,
        val uuid: String?,
        val comment: String,
        val geoLocation: JsonLocation?,
        val prices: List<JsonPrice>?,
        val days: List<JsonDay>? = null,
        val departureOffset: Int?
)

data class JsonRoute(
        val name: String,
        val uuid: String?,
        val days: List<JsonDay>? = null,
        val stations: List<JsonStation>,
        val returnStation: JsonStation
)

data class ScheduleFile(
        val routes: List<JsonRoute>
)