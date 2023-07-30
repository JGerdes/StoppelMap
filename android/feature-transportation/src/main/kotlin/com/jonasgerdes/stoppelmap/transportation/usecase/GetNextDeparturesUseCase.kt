package com.jonasgerdes.stoppelmap.transportation.usecase

import Departure
import com.jonasgerdes.stoppelmap.transportation.model.BusRouteDetails
import kotlinx.datetime.*
import kotlin.time.toKotlinDuration

class GetNextDeparturesUseCase {

    operator fun invoke(departures: List<Departure>, now: LocalDateTime) = departures
        .filter { it.time > now }
        .sortedBy { it.time }
        .take(3)
        .map {
            val difference = java.time.Duration.between(
                now.toJavaLocalDateTime(),
                it.time.toJavaLocalDateTime()
            ).toKotlinDuration()
            when {
                difference.inWholeMinutes < 1 ->
                    BusRouteDetails.DepartureTime.Immediately

                difference.inWholeMinutes < 30 ->
                    BusRouteDetails.DepartureTime.InMinutes(difference.inWholeMinutes.toInt())

                it.time.isConsideredSameDay(now.date) ->
                    BusRouteDetails.DepartureTime.Today(it.time.time)

                it.time.isConsideredSameDay(now.date.nextDay()) ->
                    BusRouteDetails.DepartureTime.Tomorrow(it.time.time)

                difference.inWholeDays < 7 ->
                    BusRouteDetails.DepartureTime.ThisWeek(it.time)

                else -> BusRouteDetails.DepartureTime.Absolute(dateTime = it.time)
            }
        }
}


private fun LocalDate.nextDay() = plus(1, DateTimeUnit.DAY)

private fun LocalDateTime.isConsideredSameDay(other: LocalDate) =
// Today or tomorrow but in the night so we consider it today still.
    // TODO: Make this calculation somehow consistent throughout the app & preparation
    date == other || date == other.plus(1, DateTimeUnit.DAY) && time.hour < 4
