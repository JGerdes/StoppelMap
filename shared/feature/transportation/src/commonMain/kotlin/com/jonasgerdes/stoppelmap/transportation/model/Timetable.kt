package com.jonasgerdes.stoppelmap.transportation.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format
import kotlinx.datetime.format.char

data class Timetable(
    val departureDays: List<DepartureDay>,
    val daySegments: List<DaySegment>
) {

    data class DepartureDay(
        val date: LocalDate,
        val isToday: Boolean,
    )

    data class DaySegment(
        val type: DaySegmentType,
        val departureSlots: List<DepartureSlot>
    )

    data class DepartureSlot(
        val departures: List<Departure?>
    ) {
        // For iOS
        fun formattedTimes() = departures.map {
            FormattedDeparture(
                time = it?.time?.time?.format(timeFormatter) ?: " ",
                isInPast = it?.isInPast ?: false
            )
        }
    }

    data class FormattedDeparture(
        val time: String,
        val isInPast: Boolean,
    )

    data class Departure(
        val time: LocalDateTime,
        val isInPast: Boolean,
    )

    enum class DaySegmentType(val startTimeInclusive: LocalTime, val endTimeExclusive: LocalTime) {
        MORNING(LocalTime(6, 0), LocalTime(13, 0)),
        AFTERNOON(LocalTime(13, 0), LocalTime(18, 0)),
        EVENING(LocalTime(18, 0), LocalTime(23, 0)),
        NIGHT(LocalTime(23, 0), LocalTime(6, 0))
    }
}

private val timeFormatter = LocalTime.Format {
    hour()
    char(':')
    minute()
}
