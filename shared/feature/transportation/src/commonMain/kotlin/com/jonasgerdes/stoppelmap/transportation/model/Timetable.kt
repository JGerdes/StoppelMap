package com.jonasgerdes.stoppelmap.transportation.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

data class Timetable(
    val departureDays: List<LocalDate>,
    val segments: List<DaySegment>
) {

    data class DaySegment(
        val type: DaySegmentType,
        val departureSlots: List<DepartureSlot>
    )

    data class DepartureSlot(
        val departures: List<Departure?>
    )

    enum class DaySegmentType(val startTimeInclusive: LocalTime, val endTimeExclusive: LocalTime) {
        MORNING(LocalTime(6, 0), LocalTime(13, 0)),
        AFTERNOON(LocalTime(13, 0), LocalTime(18, 0)),
        EVENING(LocalTime(18, 0), LocalTime(23, 0)),
        NIGHT(LocalTime(23, 0), LocalTime(6, 0))
    }
}
