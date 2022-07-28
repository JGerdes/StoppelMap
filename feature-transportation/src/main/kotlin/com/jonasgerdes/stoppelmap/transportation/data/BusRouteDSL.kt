package com.jonasgerdes.stoppelmap.transportation.data

import com.jonasgerdes.stoppelmap.transportation.data.model.BusRoute
import com.jonasgerdes.stoppelmap.transportation.data.model.DepartureDay
import com.jonasgerdes.stoppelmap.transportation.data.model.Price
import com.jonasgerdes.stoppelmap.transportation.data.model.Station
import kotlinx.datetime.*
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class BusRouteScope {
    var id: String? = null
    var title: String? = null
        set(value) {
            if (id == null && value != null) {
                id = "22-" + value.trim().lowercase().replace(" ", "-")
            }
            field = value
        }
    val stations: MutableList<Station> = mutableListOf()
    val returnStations: MutableList<Station> = mutableListOf()

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
        )
    }

class StationScope(private val routeScope: BusRouteScope) {
    var id: String? = null
    var title: String? = null
        set(value) {
            if (id == null && value != null) {
                id = routeScope.id + "-" + value.trim().lowercase().replace(" ", "-")
            }
            field = value
        }
    var prices: List<Price> = routeScope.fixedPrices ?: emptyList()
    var departures: MutableList<DepartureDay> = mutableListOf()

    fun departuresAfterPreviousStation(offset: Duration) {
        departures = routeScope.stations.last().departures.map { departureDay ->
            departureDay.copy(
                departures = departureDay.departures.map { departure ->
                    departure.toInstant(timeZoneStoppelmarkt).plus(offset).toLocalDateTime(
                        timeZoneStoppelmarkt
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
                    departures = it.departures.distinct(),
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
    val departures: MutableList<LocalDateTime> = mutableListOf()
    var laterDeparturesOnDemand: Boolean = false

    fun departures(vararg departureTimes: String) {
        departures.addAll(departureTimes.map { it.toLocalTime().atDay() })
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
            departures.add(next)
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


data class StartAndStep(
    val start: LocalTime,
    val step: Duration
)


private val timeZoneStoppelmarkt = TimeZone.of("Europe/Berlin")
private const val firstHourOfNextDay = 6

val Int.Minutes get() = toDuration(DurationUnit.MINUTES)
