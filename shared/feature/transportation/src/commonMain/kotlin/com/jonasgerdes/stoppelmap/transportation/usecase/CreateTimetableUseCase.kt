package com.jonasgerdes.stoppelmap.transportation.usecase

import com.jonasgerdes.stoppelmap.base.contract.ClockProvider
import com.jonasgerdes.stoppelmap.transportation.model.Departure
import com.jonasgerdes.stoppelmap.transportation.model.DepartureDay
import com.jonasgerdes.stoppelmap.transportation.model.Timetable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalTime
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class CreateTimetableUseCase(
    private val clockProvider: ClockProvider
) {

    suspend operator fun invoke(departures: List<DepartureDay>): Timetable =
        withContext(Dispatchers.Default) {
            val allDepartures: List<DepartureWithDay> =
                departures.flatMap { day -> day.departures.map { DepartureWithDay(it, day) } }
            val allSlots: Map<LocalTime, List<DepartureWithDay>> =
                allDepartures.groupBy { it.departure.time.time }

            val now = clockProvider.nowAsInstant()
            val today = clockProvider.toLocalDateTime(now).date

            Timetable(
                departureDays = departures.map {
                    Timetable.DepartureDay(
                        date = it.day,
                        isToday = it.day == today,
                    )
                },
                daySegments = Timetable.DaySegmentType.entries.map { daySegmentType ->
                    Timetable.DaySegment(
                        type = daySegmentType,
                        departureSlots =
                            allSlots.keys
                                .filter { daySegmentType.containsTime(it) }
                                .map { slotTime ->
                                    val departuresWithDay = allSlots[slotTime]!!
                                    Timetable.DepartureSlot(departures = departures.associate { day ->
                                        day.day to departuresWithDay.find { it.day.day == day.day }?.departure
                                    }.values.toList().map {
                                        it?.let {
                                            Timetable.Departure(
                                                it.time,
                                                isInPast = clockProvider.toInstant(it.time) < now
                                            )
                                        }
                                    })
                                }
                                .sortedBy {
                                    with(clockProvider) {
                                        val firstIndex = it.departures.indexOfFirst { it != null }
                                        it.departures[firstIndex]!!.time.asInstant().minus(
                                            firstIndex.toDuration(
                                                DurationUnit.DAYS
                                            )
                                        ).asLocalDateTime()
                                    }
                                }
                    )
                }.filter { it.departureSlots.isNotEmpty() }
            )
        }
}

private data class DepartureWithDay(
    val departure: Departure,
    val day: DepartureDay
)

private fun Timetable.DaySegmentType.containsTime(localTime: LocalTime) =
    if (startTimeInclusive < endTimeExclusive) {
        localTime >= startTimeInclusive && localTime < endTimeExclusive
    } else {
        localTime >= startTimeInclusive || localTime < endTimeExclusive
    }
