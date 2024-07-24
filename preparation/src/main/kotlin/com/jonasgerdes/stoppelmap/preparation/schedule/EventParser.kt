package com.jonasgerdes.stoppelmap.preparation.schedule

import com.jonasgerdes.stoppelmap.data.dataModule
import com.jonasgerdes.stoppelmap.dto.Locales.de
import com.jonasgerdes.stoppelmap.preparation.Settings
import com.jonasgerdes.stoppelmap.preparation.preparationModule
import com.jonasgerdes.stoppelmap.preperation.asSlug
import com.jonasgerdes.stoppelmap.preperation.entity.JsonEvent
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.char
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToStream
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import java.io.File

//format: 15.08.2019 18:01
//val EVENT_DATETIME_FORMAT = DateTimeFormatter.ofPattern("EE dd.MM.yyyy HH:mm", Locale.GERMAN)
val websiteEventLocalDateTimeFormat = LocalDateTime.Format {
    dayOfMonth()
    char('.')
    monthNumber()
    char('.')
    year()
    char(' ')
    hour()
    char(':')
    minute()
}
const val baseUrl = "https://www.stoppelmarkt.de"

data class EventLocation(
    val title: String,
    val url: String,
    val shortDescription: String? = null,
    val description: String? = null,
    val events: List<Event>? = null
)

data class Event(
    val title: String,
    val start: LocalDateTime,
    val description: String?
)


fun parsePartyTentEvents(): List<EventLocation> {

    val url = baseUrl + "/zelte/"

    println("start parsing website for events")
    return Jsoup.connect(url).get().body()
        .select(".ce-textpic")
        .toList()
        .filter { it.select("a").isNotEmpty() }
        .map {
            EventLocation(
                title = it.select("a").text(),
                url = it.select("a").attr("href"),
                shortDescription = it.select("p").firstOrNull()?.html()
            )
        }.map { it.fillWithEvents() }
}

fun EventLocation.fillWithEvents(): EventLocation {
    val body = Jsoup.connect(baseUrl + url).get().body()
    println("fetch and parse ${baseUrl + url}")

    val eventContainer = body.select(".news-list-view")
    val events =
        try {
            parseEvents(eventContainer)
        } catch (e: Exception) {
            print("Error while parsing events: $e")
            emptyList()
        }

    return copy(
        description = body.select(".ce-bodytext p").map { it.html() }.joinToString("<br><br>"),
        events = events
    )
}


fun parseEvents(eventContainer: Elements?): List<Event>? {
    val element = eventContainer?.firstOrNull() ?: return emptyList()

    return element.select(".news-list-item").map { item ->
        val date: LocalDateTime = item.select("time").attr("datetime").let {
            val date = it.drop(3)
            websiteEventLocalDateTimeFormat.parse(date)
        }

        val link = item.select("h3 > a")
        val title = link.attr("title")
        val detailUrl = link.attr("href")

        val detailBody = Jsoup.connect(baseUrl + detailUrl).get().body()

        val detailHtml = detailBody.select(".news-text-wrap").html()

        Event(
            title = title,
            start = date,
            description = detailHtml.takeIf { it.isNotBlank() }
        )
    }

}


data class EventWrapper(val events: List<JsonEvent>)

fun writeEventsToFile(
    descriptionFolder: File,
    eventsFile: File,
    eventLocations: List<EventLocation>
) {

    eventsFile.run {
        if (exists()) delete()
        createNewFile()
    }

    val events = mutableListOf<com.jonasgerdes.stoppelmap.dto.data.Event>()

    eventLocations.forEach { location ->
        val stallSlug = location.url
            .removePrefix("https://www.stoppelmarkt.de/")
            .removePrefix("/zelte")
            .removePrefix("/")
            .removeSuffix("/")
            .removeSuffix(".html")
        val file = File(descriptionFolder, "$stallSlug.html")
        if (location.description != null) {
            println("Write description of [$stallSlug] to $file (url was ${location.url})")
            file.writeText(location.description)
        }

        events += location.events
            ?.mapIndexed { index, event ->
                com.jonasgerdes.stoppelmap.dto.data.Event(
                    slug = "${stallSlug}_${event.title.asSlug()}_${
                        index.toString().padStart(2, '0')
                    }",
                    name = event.title,
                    start = event.start,
                    end = null,
                    location = stallSlug,
                    description = event.description?.let {
                        mapOf(de to it)
                    },
                    participants = listOf(),
                    websites = listOf(),
                    isOfficial = false,
                )
            } ?: emptyList()
    }
    Json {
        prettyPrint = true
        explicitNulls = true
        encodeDefaults = true
    }.encodeToStream(events, eventsFile.outputStream())

}

class EventParser : KoinComponent {
    val settings: Settings by inject()

    fun fetchAndParseEvents() {
        val folder = settings.descriptionFolder.apply { mkdirs() }
        val eventsFile = settings.fetchedEventsFile.apply { createNewFile() }
        val marquees = parsePartyTentEvents()

        writeEventsToFile(
            descriptionFolder = folder,
            eventsFile = eventsFile,
            eventLocations = marquees
        )
    }
}


fun main(args: Array<String>) {
    startKoin {
        modules(
            dataModule,
            preparationModule
        )
    }

    EventParser().fetchAndParseEvents()
}
