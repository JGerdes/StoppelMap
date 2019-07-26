package com.jonasgerdes.stoppelmap.preperation.parse

import com.google.gson.GsonBuilder
import com.google.gson.stream.JsonReader
import com.jonasgerdes.stoppelmap.preperation.Data
import com.jonasgerdes.stoppelmap.preperation.asSlug
import com.jonasgerdes.stoppelmap.preperation.entity.*
import java.io.File
import java.util.*


fun Data.parseBusSchedule(files: List<File>) {

    System.out.println("Reading transport schedule from ${files.size} files")

    val gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd hh:mm:ss")
        .create()
    files.map { JsonReader(it.reader()) }
        .map { gson.fromJson<ScheduleFile>(it, ScheduleFile::class.java) }
        .flatMap { it.routes }
        .forEach { jsonRoute ->
            val route = Route(
                slug = jsonRoute.uuid ?: jsonRoute.name.asSlug(),
                name = jsonRoute.name
            )
            jsonRoute.stations.forEach {
                addStation(it, route, false, jsonRoute.days)
            }
            addStation(jsonRoute.returnStation, route, true)
            routes += route
        }
}

private fun Data.addStation(it: JsonStation, route: Route, isReturnStation: Boolean, days: List<JsonDay>? = null) {
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
        departures += it.days.flatMap { it.departures }
            .map {
                Departure(
                    station = station.slug,
                    time = it.time
                )
            }
    } else if (days != null && it.departureOffset != null) {
        val offset = it.departureOffset
        departures += days.flatMap { it.departures }
            .map {
                val time = Calendar.getInstance()
                time.time = it.time
                time.add(Calendar.SECOND, offset)
                Departure(
                    station = station.slug,
                    time = time.time
                )
            }
    } else {
        throw RuntimeException("Invalid departure times. No days ($days) or departureOffset(${it.departureOffset}) defined for ${it.name} on ${route.name} (${it.uuid})")
    }
    stations += station
}
