package com.jonasgerdes.stoppelmap.model

import com.jonasgerdes.stoppelmap.model.parse.parseBusSchedule
import com.jonasgerdes.stoppelmap.model.parse.parseEventSchedule
import com.jonasgerdes.stoppelmap.model.parse.parseGeoJson
import java.io.File

object Settings {
    val assets = File("app/src/main/assets/")
    val data = File("preparation/src/main/java/data")

    val database = File(assets, "stoppelmap.db")
    val geoOutput = File(assets, "stoppelmap.geojson")

    val geoInput = File("$data/map", "stoma-2017.geojson")
    val scheduleDir = File(data, "schedule")
    val busDir = File("$data/transportation", "bus")
}


fun main(args: Array<String>) {
    println("Start parsing @ ${currentTime()}")

    if (Settings.database.exists()) {
        Settings.database.delete()
    }

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
                .filter { it.name != "template.json" })
    }.insertInto(db)

    System.out.println("finished @ ${currentTime()}")
}