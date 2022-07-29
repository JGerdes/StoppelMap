package com.jonasgerdes.stoppelmap.transportation.usecase

import com.jonasgerdes.stoppelmap.transportation.model.Departure
import com.jonasgerdes.stoppelmap.transportation.model.DepartureDay
import com.jonasgerdes.stoppelmap.transportation.model.Timetable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalTime

class CreateTimetableUseCase {

    suspend operator fun invoke(departures: List<DepartureDay>): Timetable =
        withContext(Dispatchers.Default) {
            val allDepartures: List<DepartureWithDay> =
                departures.flatMap { day -> day.departures.map { DepartureWithDay(it, day) } }
            val allSlots: Map<LocalTime, List<DepartureWithDay>> =
                allDepartures.groupBy { it.departure.time.time }

            Timetable(
                departureDays = departures.map { it.day },
                segments = Timetable.DaySegmentType.values().map { daySegmentType ->
                    Timetable.DaySegment(
                        type = daySegmentType,
                        departureSlots =
                        allSlots.keys.filter { daySegmentType.containsTime(it) }
                            .map { slotTime ->
                                val departuresWithDay = allSlots[slotTime]!!
                                Timetable.DepartureSlot(departures = departures.associate { day -> day.day to departuresWithDay.find { it.day == day }?.departure }.values.toList())
                            }
                            .sortedBy { it.departures.filterNotNull().first().time }
                    )
                }
            )
        }
}

private data class DepartureWithDay(
    val departure: Departure,
    val day: DepartureDay
)

private fun Timetable.DaySegmentType.containsTime(localTime: LocalTime) =
    if (startTimeInclusive < endTimeExclusive) {
        localTime > startTimeInclusive && localTime < endTimeExclusive
    } else {
        localTime > startTimeInclusive || localTime < endTimeExclusive
    }
