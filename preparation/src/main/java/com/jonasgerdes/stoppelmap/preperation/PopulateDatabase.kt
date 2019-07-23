package com.jonasgerdes.stoppelmap.preperation

import com.jonasgerdes.stoppelmap.preperation.parse.parseBusSchedule
import com.jonasgerdes.stoppelmap.preperation.parse.parseEventSchedule
import com.jonasgerdes.stoppelmap.preperation.parse.parseGeoJson
import java.io.File

object Settings {
    val dataAssets = File("../data/src/main/assets/")
    val mapAssets = File("../map/src/main/assets/")
    val data = File("../preparation/src/main/resources")

    val database = File(dataAssets, "stoppelmap.db")
    val geoOutput = File(mapAssets, "mapdata.geojson")

    val geoInput = File("$data/map", "StoppelMap2018.geojson")
    val scheduleDir = File(data, "schedule")
    val busDir = File("$data/transportation", "bus")
}


fun main(args: Array<String>) {
    println("Start parsing @ ${currentTime()}")
    val file = File("")
    println("root dir: ${file.absolutePath}")

    if (Settings.database.exists()) {
        val success = Settings.database.delete()
        if (success) {
            println("Successfully deleted old database file")
        } else {
            throw RuntimeException(
                "Couldn't delete ${Settings.database.absolutePath}, " +
                        "is it opened in any program?"
            )
        }
    }

    val db = openSQLite(Settings.database)!!
    Data().apply {
        parseGeoJson(
            Settings.geoInput,
            Settings.geoOutput
        )
        parseEventSchedule(
            Settings.scheduleDir.listFiles()
                .asList()
                .filter { it.extension == "json" }
                .filter { it.name != "template.json" })
        parseBusSchedule(
            Settings.busDir.listFiles()
                .asList()
                .filter { it.extension == "json" }
                .filter { it.name != "template.json" }
                .filter { it.name != "new_template.json" })
    }.insertInto(db)

    System.out.println("finished @ ${currentTime()}")
}
