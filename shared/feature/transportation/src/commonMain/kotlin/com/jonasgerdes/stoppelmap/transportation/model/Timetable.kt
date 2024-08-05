package com.jonasgerdes.stoppelmap.transportation.model

import com.jonasgerdes.stoppelmap.data.transportation.Departure
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format
import kotlinx.datetime.format.char

data class Timetable(
    val departureDays: List<LocalDate>,
    val daySegments: List<DaySegment>
) {

    data class DaySegment(
        val type: DaySegmentType,
        val departureSlots: List<DepartureSlot>
    )

    data class DepartureSlot(
        val departures: List<Departure?>
    ) {
        fun formattedTimes() = departures.map {
            it?.time?.time?.format(timeFormatter) ?: " "
        }
    }

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
