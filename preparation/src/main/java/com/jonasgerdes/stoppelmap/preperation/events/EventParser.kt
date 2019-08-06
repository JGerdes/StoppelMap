package com.jonasgerdes.stoppelmap.preperation.events

import com.google.gson.GsonBuilder
import com.google.gson.stream.JsonWriter
import com.jonasgerdes.stoppelmap.preperation.Settings
import com.jonasgerdes.stoppelmap.preperation.asSlug
import com.jonasgerdes.stoppelmap.preperation.entity.JsonEvent
import com.jonasgerdes.stoppelmap.preperation.entity.marqueMappings
import com.jonasgerdes.stoppelmap.preperation.genericType
import com.jonasgerdes.stoppelmap.preperation.parse.eventJsonFormat
import com.jonasgerdes.stoppelmap.preperation.toOffsetAtStoppelmarkt
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.io.File
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*

//format: Do 15.08.2019 18:01
val EVENT_DATETIME_FORMAT = DateTimeFormatter.ofPattern("EE dd.MM.yyyy HH:mm", Locale.GERMAN)
const val baseUrl = "https://www.stoppelmarkt.de/"

data class Marquee(
    val title: String,
    val url: String,
    val shortDescription: String? = null,
    val description: String? = null,
    val events: List<Event>? = null
)

data class Event(
    val title: String,
    val start: OffsetDateTime,
    val description: String?
)


fun parseMarqueeEvents(): List<Marquee> {

    val url = baseUrl + "zelte/"

    println("start parsing website for events")
    return Jsoup.connect(url).get().body()
        .select(".ce-textpic")
        .map {
            Marquee(
                title = it.select("a").text(),
                url = it.select("a").attr("href"),
                shortDescription = it.select("p").firstOrNull()?.html()
            )
        }.map { it.fillWithEvents() }
}

fun Marquee.fillWithEvents(): Marquee {
    val body = Jsoup.connect(baseUrl + url).get().body()
    println("fetch and parse ${baseUrl + url}")

    val eventContainer = body.select(".news-list-view")
    val events = parseEvents(eventContainer)

    return copy(
        description = body.select(".ce-bodytext p").map { it.html() }.joinToString("<br><br>"),
        events = events
    )
}


fun parseEvents(eventContainer: Elements?): List<Event>? {
    val element = eventContainer?.firstOrNull() ?: return emptyList()

    return element.select(".news-list-item").map { item ->
        val date: OffsetDateTime = item.select("time").attr("datetime").let {
            LocalDateTime.parse(it, EVENT_DATETIME_FORMAT).toOffsetAtStoppelmarkt()
        }

        val link = item.select("h3 > a")
        val title = link.attr("title")
        val detailUrl = link.attr("href")

        val detailBody = Jsoup.connect(baseUrl + detailUrl).get().body()

        val detailHtml = detailBody.select(".news-text-wrap").html()

        Event(
            title = title,
            start = date,
            description = detailHtml
        )
    }

}


data class EventWrapper(val events: List<JsonEvent>)

fun writeEventsToFile(descriptionFolder: File, eventsFile: File, marquees: List<Marquee>) {

    eventsFile.run {
        if (exists()) delete()
        createNewFile()
    }

    val events = mutableListOf<JsonEvent>()

    marquees.forEach { marquee ->
        val stallSlug = marqueMappings[marquee.url]
        val file = File(descriptionFolder, "$stallSlug.html")
        if (marquee.description != null) {
            file.writeText(marquee.description)
        }

        events += marquee.events?.map { event ->
            JsonEvent(
                name = event.title,
                start = event.start.format(eventJsonFormat),
                end = null,
                description = event.description,
                uuid = stallSlug + event.title.asSlug()
            )
        } ?: emptyList()
    }

    val wrapper = EventWrapper(events)

    val gson = GsonBuilder()
        .setPrettyPrinting()
        .create()

    val jsonWriter = JsonWriter(eventsFile.writer())
    gson.toJson(wrapper, genericType<EventWrapper>(), jsonWriter)
    jsonWriter.close()

}


fun main(args: Array<String>) {
    val marquees = parseMarqueeEvents()
    writeEventsToFile(
        descriptionFolder = Settings.descriptionFolder,
        eventsFile = Settings.eventsFile,
        marquees = marquees
    )
}