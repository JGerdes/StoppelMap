package com.jonasgerdes.stoppelmap.bus.station

import com.jonasgerdes.stoppelmap.model.transportation.Station
import com.jonasgerdes.stoppelmap.model.transportation.TransportPrice
import org.threeten.bp.DayOfWeek
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.OffsetTime

data class StationState(
        val station: Station,
        val departureItems: List<DepartureGridItem>,
        val prices: List<TransportPrice>
)

data class TimeSpan(
        val type: Type,
        val slots: List<TimeSlot>
) {
    enum class Type {
        MORNING, AFTERNOON, EVENING, NIGHT
    }
}

data class TimeSlot(
        val label: String,
        val hour: OffsetTime,
        val days: List<DayDepartures>
)

data class DayDepartures(
        val day: Day,
        val departures: List<OffsetDateTime>
)

enum class Day(val dayOfWeek: DayOfWeek) {
    Thursday(DayOfWeek.THURSDAY),
    Friday(DayOfWeek.FRIDAY),
    Saturday(DayOfWeek.SATURDAY),
    Sunday(DayOfWeek.SUNDAY),
    Monday(DayOfWeek.MONDAY),
    Tuesday(DayOfWeek.TUESDAY)
}

fun List<TimeSpan>.toGridItems(): List<DepartureGridItem> {
    val items = mutableListOf<DepartureGridItem>()

    forEach { span ->
        items.add(DepartureGridItem.TimeSpanHeader(span.type))
        span.slots
                .filter { it.days.isNotEmpty() }
                .forEach {
                    val maxSize = it.days.map { it.departures.size }.sortedDescending().first()
                    for (i in 0 until maxSize) {
                        it.days.forEach {
                            items.add(it.departures.getOrNull(i)?.let { DepartureGridItem.Departure(it) }
                                    ?: DepartureGridItem.Empty)
                        }
                    }
                }
    }
    return items.toList()
}

sealed class DepartureGridItem {
    data class TimeSpanHeader(val type: TimeSpan.Type) : DepartureGridItem()
    data class TimeSlotLabel(val label: String) : DepartureGridItem()
    data class Departure(val departure: OffsetDateTime) : DepartureGridItem()
    object Empty : DepartureGridItem()

}