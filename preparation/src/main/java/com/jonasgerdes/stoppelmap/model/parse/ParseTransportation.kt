package com.jonasgerdes.stoppelmap.model.parse

import com.google.gson.GsonBuilder
import com.google.gson.stream.JsonReader
import com.jonasgerdes.stoppelmap.model.Data
import com.jonasgerdes.stoppelmap.model.entity.*
import java.io.File


fun Data.parseBusSchedule(files: List<File>) {

    System.out.println("Reading transport schedule from ${files.size} files")

    val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd hh:mm:ss")
            .create()
    files.map { JsonReader(it.reader()) }
            .map { gson.fromJson<ScheduleFile>(it, ScheduleFile::class.java) }
            .flatMap { it.routes }
            .forEach {
                val route = Route(
                        slug = it.uuid,
                        name = it.name
                )
                it.stations.forEach {
                    addStation(it, route, false)
                }
                addStation(it.returnStation, route, true)
                routes += route
            }
}

private fun Data.addStation(it: JsonStation, route: Route, isReturnStation:Boolean) {
    val station = Station(
            slug = it.uuid,
            name = it.name,
            latitude = it.geoLocation?.lat,
            longitude = it.geoLocation?.lng,
            comment = it.comment,
            route = route.slug,
            is_return = isReturnStation
    )
    transportPrices += it.prices.map {
        TransportPrice(
                station = station.slug,
                type = it.type,
                price = it.price
        )
    }
    departures += it.days.flatMap { it.departures }
            .map {
                Departure(
                        station = station.slug,
                        time = it.time
                )
            }
    stations += station
}
