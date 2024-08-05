package com.jonasgerdes.stoppelmap.transportation.model

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

sealed class DepartureTime {
    object Immediately : DepartureTime()
    data class InMinutes(val minutes: Int) : DepartureTime()
    data class Today(val time: LocalTime) : DepartureTime()
    data class Tomorrow(val time: LocalTime) : DepartureTime()
    data class ThisWeek(val dateTime: LocalDateTime) : DepartureTime()
    data class Absolute(val dateTime: LocalDateTime) : DepartureTime()
}