package com.jonasgerdes.stoppelmap.preperation

import com.google.gson.GsonBuilder
import com.google.gson.stream.JsonWriter
import com.jonasgerdes.stoppelmap.preperation.entity.SharableStall
import com.jonasgerdes.stoppelmap.preperation.parse.parseBusSchedule
import com.jonasgerdes.stoppelmap.preperation.parse.parseEventSchedule
import com.jonasgerdes.stoppelmap.preperation.parse.parseGeoJson
import java.io.File
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

object Settings {
    val dataAssets = File("../data/src/main/assets/")
    val mapAssets = File("../map/src/main/assets/")
    val data = File("../preparation/src/main/resources")
    val databaseSchemaLocation = File("../schemas/com.jonasgerdes.stoppelmap.data.StoppelmapDatabase")
    val databaseSchemaFile = databaseSchemaLocation.listFiles()
        .sortedBy { it.name.replaceFirst(".json", "").toInt() }.last()

    val database = File(dataAssets, "stoppelmap.db")
    val geoOutput = File(mapAssets, "mapdata.geojson")

    val geoInput = File("$data/map", "stoma-2019.geojson")
    val serverData = File("$data", "server").apply { mkdirs() }
    val shareJson = File("$serverData", "2019.json")
    val scheduleDir = File(data, "schedule")
    val manualDir = File(scheduleDir, "manual")
    val descriptionFolder = File(data, "descriptions").apply { mkdirs() }
    val eventsFile = File(scheduleDir, "fetched.json").apply {
        if (!exists())
            createNewFile()
    }
    val busDir = File("$data/transportation", "bus")
}


fun main(args: Array<String>) {
    println("Start parsing @ ${currentTime()}")
    println("Start parsing @ ${OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)}")
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
    val gson = GsonBuilder().create()
    println("created database from schema " + Settings.databaseSchemaFile.path)
    db.createTablesFromSchemaFile(Settings.databaseSchemaFile, gson)
    println("created database, start parsing")

    val data = Data().apply {
        parseGeoJson(
            Settings.geoInput,
            Settings.geoOutput,
            Settings.descriptionFolder
        )
        parseEventSchedule(
            Settings.manualDir.listFiles().asList()
                .filter { it.extension == "json" }
                .filter { it.name != "template.json" }
                    + listOf(Settings.eventsFile)
        )
        parseBusSchedule(
            Settings.busDir.listFiles()
                .asList()
                .filter { it.extension == "csv" }
                .filter { it.name != "template.csv" }
        )
    }

    data.insertInto(db)

    createShareJson(Settings.shareJson, data)

    System.out.println("finished @ ${currentTime()}")
}

fun createShareJson(shareJson: File, data: Data) {
    val gson = GsonBuilder()
        .setPrettyPrinting()
        .create()

    if (shareJson.exists()) {
        shareJson.delete()
    }
    shareJson.createNewFile()


    val shareJsonData = data.stalls
        .filter { it.name != null }
        .map {
            SharableStall(
                slug = it.slug,
                name = it.name!!,
                type = it.type
            )
        }

    val jsonWriter = JsonWriter(shareJson.writer())
    gson.toJson(shareJsonData, genericType<List<SharableStall>>(), jsonWriter)
    jsonWriter.close()
}
