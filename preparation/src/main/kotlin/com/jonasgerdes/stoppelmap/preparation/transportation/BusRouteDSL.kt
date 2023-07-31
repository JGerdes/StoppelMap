package com.jonasgerdes.stoppelmap.preparation.transportation

import com.jonasgerdes.stoppelmap.data.model.database.RouteType
import com.jonasgerdes.stoppelmap.transportation.model.Departure
import com.jonasgerdes.stoppelmap.transportation.model.DepartureDay
import com.jonasgerdes.stoppelmap.transportation.model.Price
import com.jonasgerdes.stoppelmap.transportation.model.Route
import com.jonasgerdes.stoppelmap.transportation.model.Station
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.toLocalTime
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class BusRouteScope {
    var id: String? = null
    var title: String? = null
        set(value) {
            if (id == null && value != null) {
                id = value.toSlug()
            }
            field = value
        }
    val stations: MutableList<Station> = mutableListOf()
    val returnStations: MutableList<Station> = mutableListOf()
    var additionalInfo: String? = null

    var fixedPrices: List<Price>? = null
    var type: RouteType = RouteType.Bus
}

fun createBusRoute(builder: BusRouteScope.() -> Unit) =
    BusRouteScope().apply {
        builder()
    }.let {
        Route(
            id = it.id!!,
            title = it.title!!,
            stations = it.stations.toList(),
            returnStations = it.returnStations,
            additionalInfo = it.additionalInfo,
            type = it.type
        )
    }

class StationScope(private val routeScope: BusRouteScope) {
    var id: String? = null
    var title: String? = null
        set(value) {
            if (id == null && value != null) {
                id = routeScope.id + "-" + value.toSlug()
            }
            field = value
        }
    var prices: List<Price> = routeScope.fixedPrices ?: emptyList()
    var departures: MutableList<DepartureDay> = mutableListOf()
    var isDestination: Boolean = false
    var isNew: Boolean = false

    fun departuresAfterPreviousStation(offset: Duration) {
        departures = routeScope.stations.last().departures.map { departureDay ->
            departureDay.copy(
                departures = departureDay.departures.map { departure ->
                    departure.copy(
                        time = departure.time.toInstant(timeZoneStoppelmarkt).plus(offset)
                            .toLocalDateTime(
                                timeZoneStoppelmarkt
                            )
                    )
                }
            )
        }.toMutableList()
    }

    fun thursday(builder: DepartureDayScope.() -> Unit) = departureDay(
        day = weekDayMap[DayOfWeek.THURSDAY]!!,
        builder = builder
    )

    fun thursday(vararg departures: String) = departureDay(
        day = weekDayMap[DayOfWeek.THURSDAY]!!,
        departures = departures
    )

    fun friday(builder: DepartureDayScope.() -> Unit) = departureDay(
        day = weekDayMap[DayOfWeek.FRIDAY]!!,
        builder = builder
    )

    fun friday(vararg departures: String) = departureDay(
        day = weekDayMap[DayOfWeek.FRIDAY]!!,
        departures = departures
    )

    fun saturday(builder: DepartureDayScope.() -> Unit) = departureDay(
        day = weekDayMap[DayOfWeek.SATURDAY]!!,
        builder = builder
    )

    fun saturday(vararg departures: String) = departureDay(
        day = weekDayMap[DayOfWeek.SATURDAY]!!,
        departures = departures
    )

    fun sunday(builder: DepartureDayScope.() -> Unit) = departureDay(
        day = weekDayMap[DayOfWeek.SUNDAY]!!,
        builder = builder
    )

    fun sunday(vararg departures: String) = departureDay(
        day = weekDayMap[DayOfWeek.SUNDAY]!!,
        departures = departures
    )

    fun monday(builder: DepartureDayScope.() -> Unit) = departureDay(
        day = weekDayMap[DayOfWeek.MONDAY]!!,
        builder = builder
    )

    fun monday(vararg departures: String) = departureDay(
        day = weekDayMap[DayOfWeek.MONDAY]!!,
        departures = departures
    )

    fun tuesday(builder: DepartureDayScope.() -> Unit) = departureDay(
        day = weekDayMap[DayOfWeek.TUESDAY]!!,
        builder = builder
    )

    fun tuesday(vararg departures: String) = departureDay(
        day = weekDayMap[DayOfWeek.TUESDAY]!!,
        departures = departures
    )

    private fun departureDay(day: LocalDate, vararg departures: String) {
        departureDay(day) {
            departures(*departures)
        }
    }

    private fun departureDay(day: LocalDate, builder: DepartureDayScope.() -> Unit) {
        DepartureDayScope(
            day = day
        ).apply {
            builder()
        }.let {
            departures.removeIf { it.day == day }
            departures.add(
                DepartureDay(
                    day = it.day,
                    departures = it.departures.distinctBy { it.time },
                    laterDeparturesOnDemand = it.laterDeparturesOnDemand
                )
            )
        }
    }
}

fun BusRouteScope.addStation(
    title: String? = null,
    minutesAfterPrevious: Int? = null,
    builder: (StationScope.() -> Unit)? = null
) {
    stations.add(
        StationScope(this).apply {
            this.title = title
            minutesAfterPrevious?.let {
                departuresAfterPreviousStation(it.Minutes)
            }
            builder?.invoke(this)
        }.let {
            Station(
                id = it.id!!,
                title = it.title!!,
                prices = it.prices,
                departures = it.departures,
                isDestination = it.isDestination,
                annotateAsNew = it.isNew,
            )
        }
    )
}

fun BusRouteScope.addReturnStation(builder: StationScope.() -> Unit) {
    returnStations.add(
        StationScope(this).apply {
            builder()
        }.let {
            Station(
                id = it.id!! + "-return",
                title = it.title!!,
                prices = it.prices,
                departures = it.departures,
            )
        }
    )
}

class DepartureDayScope(val day: LocalDate) {
    val departures: MutableList<Departure> = mutableListOf()
    var laterDeparturesOnDemand: Boolean = false

    fun departures(vararg departureTimes: String) {
        departures.addAll(departureTimes.map { Departure(it.toLocalTime().atDay()) })
    }

    fun departure(time: LocalDateTime, annotation: String? = null) {
        departures.add(Departure(time, annotation))
    }

    fun departure(time: String, annotation: String? = null) {
        departures.add(Departure(time.toLocalTime().atDay(), annotation))
    }

    private fun LocalTime.atDay() =
        LocalDateTime(
            date = if (hour < firstHourOfNextDay) day.plus(1, DateTimeUnit.DAY) else day,
            time = this
        )

    infix fun LocalTime.every(step: Duration) = StartAndStep(this, step)
    infix fun String.every(step: Duration) = StartAndStep(toLocalTime(), step)

    infix fun StartAndStep.until(inclusiveEnd: LocalTime) {
        var next = start.atDay()
        val end = inclusiveEnd.atDay()
        while (next <= end) {
            departures.add(Departure(next))
            next = next.toInstant(timeZoneStoppelmarkt).plus(step).toLocalDateTime(
                timeZoneStoppelmarkt
            )
        }
    }

    infix fun StartAndStep.until(inclusiveEnd: String) = until(inclusiveEnd.toLocalTime())
}

fun prices(
    adult: Int,
    children: Int,
    childrenAgeRange: Pair<Int?, Int>? = 3 to 11
) = listOf(
    Price(label = Price.PriceLabel.Adult, amountInCents = adult),
    Price(
        label = Price.PriceLabel.Children(
            minAge = childrenAgeRange?.first,
            maxAge = childrenAgeRange?.second
        ),
        amountInCents = children
    ),
)

fun StationScope.prices(
    adult: Int,
    children: Int,
    childrenAgeRange: Pair<Int?, Int>? = 3 to 11
) {
    prices =
        com.jonasgerdes.stoppelmap.preparation.transportation.prices(
            adult,
            children,
            childrenAgeRange
        )
}


data class StartAndStep(
    val start: LocalTime,
    val step: Duration
)


private val timeZoneStoppelmarkt = TimeZone.of("Europe/Berlin")
private val currentYear by lazy {
    Clock.System.now()
        .toLocalDateTime(timeZoneStoppelmarkt)
        .year
}
private val weekDayMap by lazy {
    val days = calculateDatesForYear(currentYear)
    mapOf(
        DayOfWeek.THURSDAY to days.first { it.dayOfWeek == DayOfWeek.THURSDAY },
        DayOfWeek.FRIDAY to days.first { it.dayOfWeek == DayOfWeek.FRIDAY },
        DayOfWeek.SATURDAY to days.first { it.dayOfWeek == DayOfWeek.SATURDAY },
        DayOfWeek.SUNDAY to days.first { it.dayOfWeek == DayOfWeek.SUNDAY },
        DayOfWeek.MONDAY to days.first { it.dayOfWeek == DayOfWeek.MONDAY },
        DayOfWeek.TUESDAY to days.first { it.dayOfWeek == DayOfWeek.TUESDAY },
    )
}

// TODO: Reuse SeasonProvider for this
private fun calculateDatesForYear(year: Int): List<LocalDate> {
    // On 15th of august it's always Stoppelmarkt.
    // If 15th is a wednesday, the 16th will be first day of Stoppelmarkt
    val anchorDay = LocalDate(year, Month.AUGUST, 15)
    val anchorOffset = when (anchorDay.dayOfWeek) {
        DayOfWeek.MONDAY -> -4
        DayOfWeek.TUESDAY -> -5
        DayOfWeek.WEDNESDAY -> 1
        DayOfWeek.THURSDAY -> 0
        DayOfWeek.FRIDAY -> -1
        DayOfWeek.SATURDAY -> -2
        DayOfWeek.SUNDAY -> -3
    }

    return (anchorOffset..anchorOffset + 5).map { offset ->
        anchorDay.plus(offset, DateTimeUnit.DAY)
    }
}

private const val firstHourOfNextDay = 6

val Int.Minutes get() = toDuration(DurationUnit.MINUTES)


private fun String.toSlug() =
    this
        .trim().lowercase()
        .replace("ä", "ae")
        .replace("ö", "oe")
        .replace("ü", "ue")
        .replace("ß", "s")
        .replace(Regex("\\W"), "-")
        .replace("--", "-")
        .replace("--", "-")
        .replace("--", "-")
