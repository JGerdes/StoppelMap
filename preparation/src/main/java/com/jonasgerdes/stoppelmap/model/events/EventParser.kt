package com.jonasgerdes.stoppelmap.model.events

import org.jsoup.Jsoup

data class Marquee(
        val title: String,
        val url: String,
        val shortDescription: String? = null,
        val description: String? = null,
        val events: List<Event>? = null
)

data class Event(
        val title: String
)

object Settings {
    const val baseUrl = "https://www.stoppelmarkt.de/"
}

fun parseMarqueeEvents(): List<Marquee> {

    val url = Settings.baseUrl + "zelte/"

    println("start parsing website for events")
    return Jsoup.connect(url).get().body()
            .select(".ce-textpic")
            .map {
                Marquee(
                        title = it.select("a").text(),
                        url = it.select("a").attr("href"),
                        shortDescription = it.select("p").firstOrNull()?.text()
                )
            }.map { it.fillWithEvents() }
}

fun Marquee.fillWithEvents(): Marquee {
    val body = Jsoup.connect(Settings.baseUrl + url).get().body()
    println("fetch and parse ${Settings.baseUrl + url}")
    return copy(
            description = body.select(".ce-bodytext p").map { it.text() }.joinToString("\n")
    )
}
