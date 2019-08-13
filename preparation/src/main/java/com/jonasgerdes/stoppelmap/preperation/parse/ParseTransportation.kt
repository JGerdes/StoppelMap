package com.jonasgerdes.stoppelmap.preperation.parse

import com.jonasgerdes.stoppelmap.preperation.Data
import com.jonasgerdes.stoppelmap.preperation.asSlug
import com.jonasgerdes.stoppelmap.preperation.entity.*
import com.jonasgerdes.stoppelmap.preperation.toOffsetAtStoppelmarkt
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Month
import java.time.format.DateTimeFormatter
import java.util.*


fun Data.parseBusSchedule(files: List<File>) {

    System.out.println("Reading transport schedule from ${files.size} files")

    files.forEach { file ->
        val lines = file.readLines()
        if (lines.isEmpty() || (lines.size - 1) % 10 != 0) throw IllegalArgumentException("Invalid route file ${file.path}")
        val route = Route(slug = lines.first().asSlug(), name = lines.first())

        val line = lines.listIterator()
        line.next() //skip route name
        do {
            val station = ParsedStation(name = line.next().trim())

            val location = line.next().split("|")
            if (location.size == 2 && location[0].isNotBlank() && location[1].isNotBlank()) {
                station.geoLocation = JsonLocation(
                    lat = location[0].trim().toDouble(),
                    lng = location[1].trim().toDouble()
                )
            }
            val prices = line.next().split("|")
            if (prices.isNotEmpty() && prices[0].isNotBlank() && prices[1].isNotBlank()) {
                val priceList = mutableListOf<JsonPrice>()
                for (i in 0 until prices.size step 2) {
                    priceList.add(
                        JsonPrice(
                            type = prices[i].trim(),
                            price = prices[i + 1].trim().toInt()
                        )
                    )
                }
                station.prices = priceList
            }

            val days = mutableListOf<JsonDay>()
            days.add(JsonDay(0, parseDepartures(line.next(), 0)))
            days.add(JsonDay(1, parseDepartures(line.next(), 1)))
            days.add(JsonDay(2, parseDepartures(line.next(), 2)))
            days.add(JsonDay(3, parseDepartures(line.next(), 3)))
            days.add(JsonDay(4, parseDepartures(line.next(), 4)))
            days.add(JsonDay(5, parseDepartures(line.next(), 5)))

            station.days = days
            val comment = line.next()
            station.comment = when {
                comment.isBlank() -> null
                comment == "undefined" -> null
                else -> comment.trim()
            }

            val isReturn = when {
                station.name.trim().toLowerCase(Locale.ENGLISH) == "return" -> true
                station.name.trim().toLowerCase(Locale.GERMAN) == "rückfahrt" -> true
                else -> false
            }

            addStation(station, route, isReturn, days = days)
        } while (line.hasNext())

        routes += route
    }
}

private val TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm")

fun parseDepartures(line: String, day: Int): List<JsonDeparture> {
    val dayLimit = LocalTime.of(6, 0)
    return line.split("|").filter { it.isNotBlank() }.map { timeString ->
        val time = LocalTime.parse(timeString, TIME_FORMAT)
        //todo: improve this

        var date = LocalDate.of(2019, Month.AUGUST, 15 + day)
        if (time.isBefore(dayLimit)) date.plusDays(1) //departure is actually next day but at night

        val localDateTime = LocalDateTime.of(date, time)
        localDateTime.toOffsetAtStoppelmarkt()
    }.map { JsonDeparture(it) }
}

private fun Data.addStation(it: ParsedStation, route: Route, isReturnStation: Boolean, days: List<JsonDay>? = null) {
    val station = Station(
        slug = it.uuid ?: (route.slug + it.name).asSlug(),
        name = it.name,
        latitude = it.geoLocation?.lat,
        longitude = it.geoLocation?.lng,
        comment = it.comment,
        route = route.slug,
        is_return = isReturnStation
    )
    it.prices?.run {
        transportPrices += map {
            TransportPrice(
                station = station.slug,
                type = it.type,
                price = it.price
            )
        }
    }
    if (it.days != null) {
        departures += it.days!!.flatMap {
            it.departures.map { departure ->
                Departure(
                    station = station.slug,
                    time = departure.time,
                    day = it.day
                )
            }
        }
    } else {
        throw RuntimeException("Invalid departure times for ${it.name} on ${route.name} (${it.uuid})")
    }
    stations += station
}
