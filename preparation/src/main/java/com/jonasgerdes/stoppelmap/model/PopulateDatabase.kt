package com.jonasgerdes.stoppelmap.model

import com.jonasgerdes.stoppelmap.model.events.parseMarqueeEvents
import com.jonasgerdes.stoppelmap.model.parse.parseBusSchedule
import com.jonasgerdes.stoppelmap.model.parse.parseEventSchedule
import com.jonasgerdes.stoppelmap.model.parse.parseGeoJson
import java.io.File

object Settings {
    val assets = File("app/src/main/assets/")
    val data = File("preparation/src/main/java/data")

    val database = File(assets, "stoppelmap.db")
    val geoOutput = File(assets, "stoppelmap.geojson")

    val geoInput = File("$data/map", "StoppelMap2018.geojson")
    val scheduleDir = File(data, "schedule")
    val busDir = File("$data/transportation", "bus")
}


fun main(args: Array<String>) {
    println("Start parsing @ ${currentTime()}")

    if (Settings.database.exists()) {
        val success = Settings.database.delete()
        if (success) {
            println("Successfully deleted old database file")
        } else {
            throw RuntimeException("Couldn't delete ${Settings.database.absolutePath}, " +
                    "is it opened in any program?")
        }
    }

    /*val marquees = parseMarqueeEvents()
    marquees.forEach { println(it) }
    println("got ${marquees.size}")*/

    val db = openSQLite(Settings.database)!!
    Data().apply {
        parseGeoJson(Settings.geoInput, Settings.geoOutput)
        parseEventSchedule(Settings.scheduleDir.listFiles()
                .asList()
                .filter { it.extension == "json" }
                .filter { it.name != "template.json" })
        parseBusSchedule(Settings.busDir.listFiles()
                .asList()
                .filter { it.extension == "json" }
                .filter { it.name != "template.json" }
                .filter { it.name != "new_template.json" })
    }.insertInto(db)


    System.out.println("finished @ ${currentTime()}")
}
