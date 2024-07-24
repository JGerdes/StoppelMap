package com.jonasgerdes.stoppelmap.preparation.transportation

import com.jonasgerdes.stoppelmap.dto.Localized
import com.jonasgerdes.stoppelmap.dto.data.Departure
import com.jonasgerdes.stoppelmap.dto.data.DepartureDay
import com.jonasgerdes.stoppelmap.dto.data.Fee
import com.jonasgerdes.stoppelmap.dto.data.Location
import com.jonasgerdes.stoppelmap.dto.data.Route
import com.jonasgerdes.stoppelmap.dto.data.Station
import com.jonasgerdes.stoppelmap.preparation.Settings
import com.jonasgerdes.stoppelmap.preparation.localizedString
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
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration


class BusRouteScope {
    var slug: String? = null
    var name: String? = null
        set(value) {
            if (slug == null && value != null) {
                slug = value.toSlug()
            }
            field = value
        }
    var operatorSlug: String? = null
    val stations: MutableList<Station> = mutableListOf()
    val returnStations: MutableList<Station> = mutableListOf()
    var additionalInfo: Localized<String>? = null

    var fixedPrices: List<Fee>? = null
}

fun createBusRoute(builder: BusRouteScope.() -> Unit) =
    BusRouteScope().apply {
        builder()
    }.let {
        Route(
            slug = it.slug!!,
            name = it.name!!,
            stations = it.stations + it.returnStations,
            additionalInfo = it.additionalInfo,
            operator = it.operatorSlug,
        )
    }

class StationScope(private val routeScope: BusRouteScope) {
    var slug: String? = null
    var name: String? = null
        set(value) {
            if (slug == null && value != null) {
                slug = routeScope.slug + "_" + value.toSlug()
            }
            field = value
        }
    var prices: List<Fee> = routeScope.fixedPrices ?: emptyList()
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
                    laterDepartureOnDemand = it.laterDeparturesOnDemand
                )
            )
        }
    }
}

fun BusRouteScope.addStation(
    name: String,
    minutesAfterPrevious: Int? = null,
    location: Location? = null,
    locationSlug: String? = null,
    builder: (StationScope.() -> Unit)? = null
) {
    stations.add(
        StationScope(this).apply {
            this.name = name
            minutesAfterPrevious?.let {
                departuresAfterPreviousStation(it.Minutes)
            }
            builder?.invoke(this)
        }.let {
            Station(
                slug = it.slug!!,
                name = it.name!!,
                isDestination = it.isDestination,
                isReturn = false,
                isNew = it.isNew,
                departures = it.departures,
                location = location,
                mapEntityLocation = locationSlug,
                prices = it.prices,
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
                slug = it.slug!! + "_return",
                name = it.name!!,
                isDestination = it.isDestination,
                isReturn = true,
                isNew = it.isNew,
                departures = it.departures
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

    fun departure(time: LocalDateTime, annotation: Localized<String>? = null) {
        departures.add(Departure(time, annotation))
    }

    fun departure(time: String, annotation: Localized<String>? = null) {
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
    Fee(
        name = localizedString(de = "Erwachsene", en = "Adults"),
        price = adult
    ),
    Fee(
        name = when {
            childrenAgeRange == null -> localizedString(de = "Kinder", en = "Children")
            childrenAgeRange.first == null -> localizedString(
                de = "Kinder (bis ${childrenAgeRange.second} Jahre)",
                en = "Children (up to ${childrenAgeRange.second} years old)"
            )

            else -> localizedString(
                de = "Kinder (${childrenAgeRange.first}-${childrenAgeRange.second} Jahre)",
                en = "Children (${childrenAgeRange.first}-${childrenAgeRange.second} years old)"
            )
        },
        price = children
    )
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

object RoutesContext : KoinComponent {
    private val settings: Settings by inject()
    val year: Int get() = settings.year
}

private val timeZoneStoppelmarkt = TimeZone.of("Europe/Berlin")
private val weekDayMap by lazy {
    val days = calculateDatesForYear(RoutesContext.year)
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
