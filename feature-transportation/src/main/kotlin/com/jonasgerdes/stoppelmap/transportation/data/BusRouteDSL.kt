package com.jonasgerdes.stoppelmap.transportation.data

import com.jonasgerdes.stoppelmap.transportation.model.*
import kotlinx.datetime.*
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class BusRouteScope {
    var id: String? = null
    var title: String? = null
        set(value) {
            if (id == null && value != null) {
                id = "22-" + value.toSlug()
            }
            field = value
        }
    val stations: MutableList<Station> = mutableListOf()
    val returnStations: MutableList<Station> = mutableListOf()
    var additionalInfo: String? = null

    var fixedPrices: List<Price>? = null
}

fun createBusRoute(builder: BusRouteScope.() -> Unit) =
    BusRouteScope().apply {
        builder()
    }.let {
        BusRoute(
            id = it.id!!,
            title = it.title!!,
            stations = it.stations.toList(),
            returnStations = it.returnStations,
            additionalInfo = it.additionalInfo,
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
        day = LocalDate(2022, Month.AUGUST, 11),
        builder = builder
    )

    fun thursday(vararg departures: String) = departureDay(
        day = LocalDate(2022, Month.AUGUST, 11),
        departures = departures
    )

    fun friday(builder: DepartureDayScope.() -> Unit) = departureDay(
        day = LocalDate(2022, Month.AUGUST, 12),
        builder = builder
    )

    fun friday(vararg departures: String) = departureDay(
        day = LocalDate(2022, Month.AUGUST, 12),
        departures = departures
    )

    fun saturday(builder: DepartureDayScope.() -> Unit) = departureDay(
        day = LocalDate(2022, Month.AUGUST, 13),
        builder = builder
    )

    fun saturday(vararg departures: String) = departureDay(
        day = LocalDate(2022, Month.AUGUST, 13),
        departures = departures
    )

    fun sunday(builder: DepartureDayScope.() -> Unit) = departureDay(
        day = LocalDate(2022, Month.AUGUST, 14),
        builder = builder
    )

    fun sunday(vararg departures: String) = departureDay(
        day = LocalDate(2022, Month.AUGUST, 14),
        departures = departures
    )

    fun monday(builder: DepartureDayScope.() -> Unit) = departureDay(
        day = LocalDate(2022, Month.AUGUST, 15),
        builder = builder
    )

    fun monday(vararg departures: String) = departureDay(
        day = LocalDate(2022, Month.AUGUST, 15),
        departures = departures
    )

    fun tuesday(builder: DepartureDayScope.() -> Unit) = departureDay(
        day = LocalDate(2022, Month.AUGUST, 16),
        builder = builder
    )

    fun tuesday(vararg departures: String) = departureDay(
        day = LocalDate(2022, Month.AUGUST, 16),
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
                id = it.id!!,
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
        com.jonasgerdes.stoppelmap.transportation.data.prices(adult, children, childrenAgeRange)
}


data class StartAndStep(
    val start: LocalTime,
    val step: Duration
)


private val timeZoneStoppelmarkt = TimeZone.of("Europe/Berlin")
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
