@file:OptIn(ExperimentalSerializationApi::class)

package com.jonasgerdes.stoppelmap.preparation.transportation.crawler

import com.jonasgerdes.stoppelmap.dto.data.Departure
import com.jonasgerdes.stoppelmap.dto.data.DepartureDay
import com.jonasgerdes.stoppelmap.dto.data.Route
import com.jonasgerdes.stoppelmap.dto.data.Station
import com.jonasgerdes.stoppelmap.preparation.transportation.TransportMapEntitySlugs.bahnhof
import com.jonasgerdes.stoppelmap.preparation.transportation.TransportOperatorSlugs.nwb
import com.jonasgerdes.stoppelmap.preparation.transportation.firstHourOfNextDay
import com.jonasgerdes.stoppelmap.preperation.toSlug
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format.optional
import kotlinx.datetime.minus
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.File


class TrainJourneyProcessor() {

    private val json = Json {
        prettyPrint = true
    }

    operator fun invoke(tempDir: File, seasonDates: List<LocalDate>): List<Route> {
        println("processing journeys")

        val routeStopDays: List<RouteStopDay> = tempDir
            .listFiles { it.extension == "json" && it.nameWithoutExtension.contains("journeys") }
            .mapNotNull { file ->
                file.inputStream().use {
                    val stops: List<VbnCrawler.Stop> = json.decodeFromStream(it)
                    if (stops.isEmpty()) return@use null
                    val (_, routeName, dateString) = file.nameWithoutExtension.split("_")
                    val name = routeName.replaceFirstChar { it.uppercase() }
                    val date = LocalDate.Formats.ISO.parse(dateString)
                    stops
                        .filter { it.departureScheduled != null } // Only stops without departure is Stoppelmarkt station, it has only arrival
                        .map { stop ->
                            val time = timeFormat.parse(stop.departureScheduled!!)
                            RouteStopDay(
                                routeName = name,
                                departure = LocalDateTime(date, time),
                                stop = stop,
                            )
                        }
                }
            }.flatten()

        val returnJourneys: List<ReturnJourney> = tempDir
            .listFiles { it.extension == "json" && it.nameWithoutExtension.contains("returns") }
            .mapNotNull { file ->
                file.inputStream().use {
                    val stops: List<VbnCrawler.Stop> = json.decodeFromStream(it)
                    if (stops.isEmpty()) return@use null
                    val (_, routeName, dateString) = file.nameWithoutExtension.split("_")
                    val name = routeName.replaceFirstChar { it.uppercase() }
                    val date = LocalDate.Formats.ISO.parse(dateString)

                    var returnJourneys = mutableListOf<ReturnJourney>()
                    var currentJourney: ReturnJourney? = null
                    stops.forEach { stop ->
                        if (stop.name == "Vechta-Stoppelmarkt") {
                            currentJourney?.let {
                                returnJourneys.add(it)
                            }
                            val time = timeFormat.parse(stop.departureScheduled!!)
                            val startStop = RouteStopDay(
                                routeName = name,
                                departure = LocalDateTime(date, time),
                                stop = stop,
                            )
                            currentJourney = ReturnJourney(startStop = startStop, stopsAt = emptyList())
                        } else {
                            currentJourney = currentJourney!!.copy(
                                stopsAt = currentJourney.stopsAt + stop
                            )
                        }
                    }
                    currentJourney?.let {
                        returnJourneys.add(it)
                    }
                    returnJourneys.distinctBy { it.startStop }
                }
            }.flatten()



        return routeStopDays.groupBy { it.routeName }.map { (routeName, stops) ->
            Route(
                slug = routeName.toSlug(),
                name = routeName,
                operator = nwb,
                stations = stops
                    .filter { it.stop.departureScheduled != null }.distinctBy { it.stop.name + it.departure }
                    .groupBy { it.stop.name }
                    .entries
                    .map { (_, stops) ->
                        val first = stops.first()
                        val stationName = first.stop.name
                        Station(
                            slug = "${routeName}-${stationName.toSlug()}",
                            name = stationName,
                            outward = stops.mapAndGroupToDepartureDay(seasonDates),
                            returns = returnJourneys.filter {
                                it.startStop.routeName == routeName && it.stopsAt.any { it.name == stationName }
                            }.map {
                                it.startStop
                            }.mapAndGroupToDepartureDay(seasonDates)
                        )
                    },
                arrivalStationSlug = bahnhof
            )
        }
    }
}

private fun List<RouteStopDay>.mapAndGroupToDepartureDay(seasonDates: List<LocalDate>) =
    groupBy {
        if (it.departure.time.hour < firstHourOfNextDay) {
            it.departure.date.minus(DatePeriod(days = 1))
        } else {
            it.departure.date
        }
    }.entries
        // 01:00 on Thursday is considered Wednesday, we don't want those
        .filter { (date, stops) ->
            seasonDates.contains(date).also {
                if (!it) {
                    println("Skipping $date ($stops)")
                }
            }
        }
        .map { (date, stops) ->
            DepartureDay(
                day = date,
                departures = stops.map { stop ->
                    Departure(
                        time = stop.departure,
                        arrival = null
                    )
                }.sortedBy { it.time },
                laterDepartureOnDemand = false
            )
        }.sortedBy { it.day }


data class RouteStopDay(val routeName: String, val departure: LocalDateTime, val stop: VbnCrawler.Stop)
data class ReturnJourney(val startStop: RouteStopDay, val stopsAt: List<VbnCrawler.Stop>)

private val timeFormat = LocalTime.Format {
    hour()
    minute()
    second()
    optional {
        secondFraction()
    }
}