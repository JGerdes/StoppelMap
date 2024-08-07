package com.jonasgerdes.stoppelmap.preparation.transportation

import com.jonasgerdes.stoppelmap.dto.Locales
import com.jonasgerdes.stoppelmap.dto.Localized
import com.jonasgerdes.stoppelmap.dto.data.Departure
import com.jonasgerdes.stoppelmap.dto.data.DepartureDay
import com.jonasgerdes.stoppelmap.dto.data.Fee
import com.jonasgerdes.stoppelmap.dto.data.Location
import com.jonasgerdes.stoppelmap.dto.data.Route
import com.jonasgerdes.stoppelmap.dto.data.Station
import com.jonasgerdes.stoppelmap.dto.data.Website
import com.jonasgerdes.stoppelmap.preparation.Settings
import com.jonasgerdes.stoppelmap.preparation.localizedString
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.char
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.DayOfWeek
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

private val timeFormat = LocalTime.Format {
    hour()
    char(':')
    minute()
}

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
    var arrivalStationSlug: String? = null
    val stations: MutableList<Station> = mutableListOf<Station>()
    var commonReturns: MutableList<DepartureDay> = mutableListOf()
    var additionalInfo: Localized<String>? = null

    var fixedPrices: List<Fee>? = null
    var ticketWebsites: MutableList<Website> = mutableListOf()

    fun returns(builder: DepartureScope.() -> Unit) {
        commonReturns = DepartureScope(null).apply(builder).departures
    }
}

fun createBusRoute(builder: BusRouteScope.() -> Unit) =
    BusRouteScope().apply {
        builder()
    }.let {
        Route(
            slug = it.slug!!,
            name = it.name!!,
            stations = it.stations,
            additionalInfo = it.additionalInfo,
            operator = it.operatorSlug,
            ticketWebsites = it.ticketWebsites,
            arrivalStationSlug = it.arrivalStationSlug!!,
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
    var outward: MutableList<DepartureDay> = mutableListOf()
    var returns: List<DepartureDay> = routeScope.commonReturns.toList()
    var isNew: Boolean = false
    var location: Location? = null
    var additionalInfo: Localized<String>? = null
    var ticketWebsites: List<Website> = mutableListOf()

    var durationToDestination: Duration? = null

    fun departuresAfterPreviousStation(offset: Duration) {
        outward = routeScope.stations.last().outward.map { departureDay ->
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

    fun outward(builder: DepartureScope.() -> Unit) {
        outward = DepartureScope(this).apply(builder).departures
    }

    fun returns(builder: DepartureScope.() -> Unit) {
        returns = DepartureScope(this).apply(builder).departures
    }
}

class DepartureScope(
    private val stationScope: StationScope?
) {
    var departures: MutableList<DepartureDay> = mutableListOf()

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
            day = day,
            stationScope = stationScope,
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
                isNew = it.isNew,
                additionalInfo = it.additionalInfo,
                location = it.location,
                prices = it.prices,
                ticketWebsites = it.ticketWebsites,
                outward = it.outward,
                returns = it.returns,
            )
        }
    )
}

class DepartureDayScope(val day: LocalDate, val stationScope: StationScope?) {
    val departures: MutableList<Departure> = mutableListOf()
    var laterDeparturesOnDemand: Boolean = false

    fun departures(vararg departureTimes: String) {
        departures.addAll(
            departureTimes
                .map { timeFormat.parse(it) }
                .map { time ->
                    Departure(
                        time = time.atDay(),
                        arrival = stationScope?.durationToDestination?.let { time.atDay().plus(it) },
                        annotation = null
                    )
                })
    }

    fun departure(time: LocalDateTime, annotation: Localized<String>? = null) {
        departures.add(
            Departure(
                time = time,
                arrival = stationScope?.durationToDestination?.let { time.plus(it) },
                annotation = annotation
            )
        )
    }

    fun departure(time: String, annotation: Localized<String>? = null) {
        val time = timeFormat.parse(time)
        departures.add(
            Departure(
                time = time.atDay(),
                arrival = stationScope?.durationToDestination?.let { time.atDay().plus(it) },
                annotation = null
            )
        )
    }

    private fun LocalTime.atDay() =
        LocalDateTime(
            date = if (hour < firstHourOfNextDay) day.plus(DatePeriod(days = 1)) else day,
            time = this
        )

    infix fun LocalTime.every(step: Duration) = StartAndStep(this, step)
    infix fun String.every(step: Duration) = StartAndStep(timeFormat.parse(this), step)

    infix fun StartAndStep.until(inclusiveEnd: LocalTime) {
        var next = start.atDay()
        val end = inclusiveEnd.atDay()
        while (next <= end) {
            departures.add(
                Departure(
                    time = next,
                    arrival = stationScope?.durationToDestination?.let { next.plus(it) },
                    annotation = null
                )
            )
            next = next.plus(step)
        }
    }

    infix fun StartAndStep.until(inclusiveEnd: String) = until(timeFormat.parse(inclusiveEnd))
}

fun prices(
    vararg fees: Pair<String, Int>
) = fees.map {
    Fee(
        name = mapOf(Locales.de to it.first),
        price = it.second
    )
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

fun pricesPerTrip(
    oneWay: Int,
    roundTrip: Int
) = listOf(
    Fee(
        name = localizedString(de = "Einfache Fahrt", en = "One way"),
        price = oneWay
    ),
    Fee(
        name = localizedString(de = "Hin- und Rückfahrt", en = "Round trip"),
        price = roundTrip
    )
)

fun StationScope.prices(
    vararg fees: Pair<String, Int>
) {
    prices = com.jonasgerdes.stoppelmap.preparation.transportation.prices(*fees)
}

fun StationScope.pricesPerTrip(
    oneWay: Int,
    roundTrip: Int
) {
    prices = com.jonasgerdes.stoppelmap.preparation.transportation.pricesPerTrip(oneWay = oneWay, roundTrip = roundTrip)
}

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

private fun LocalDateTime.plus(duration: Duration) =
    toInstant(timeZoneStoppelmarkt)
        .plus(duration)
        .toLocalDateTime(timeZoneStoppelmarkt)

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
        anchorDay.plus(DatePeriod(days = offset))
    }
}

const val firstHourOfNextDay = 6

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
