package com.jonasgerdes.stoppelmap.preparation.transportation.crawler

import com.jonasgerdes.stoppelmap.dto.Locales.de
import com.jonasgerdes.stoppelmap.dto.data.Departure
import com.jonasgerdes.stoppelmap.dto.data.DepartureDay
import com.jonasgerdes.stoppelmap.dto.data.Fee
import com.jonasgerdes.stoppelmap.dto.data.MapEntitySlug
import com.jonasgerdes.stoppelmap.dto.data.Route
import com.jonasgerdes.stoppelmap.dto.data.RouteSlug
import com.jonasgerdes.stoppelmap.dto.data.Station
import com.jonasgerdes.stoppelmap.preparation.transportation.TransportMapEntitySlugs
import com.jonasgerdes.stoppelmap.preparation.transportation.TransportOperatorSlugs.wilmering
import com.jonasgerdes.stoppelmap.preparation.transportation.firstHourOfNextDay
import com.jonasgerdes.stoppelmap.preperation.asSlug
import kotlinx.coroutines.delay
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format.char
import kotlinx.datetime.plus
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.parser.Parser
import kotlin.time.Duration.Companion.seconds

private const val siteMapUrl = "https://wilmering-stoma.de/wp-sitemap-taxonomies-gebiet-1.xml"
private val priceRegex = "(\\d\\d?),(\\d\\d) €".toRegex()
private val timeRegex = "(\\d\\d):(\\d\\d)".toRegex()
private val dateFormat = LocalDate.Format {
    dayOfMonth()
    char('.')
    monthNumber()
    char('.')
    year()
}
private val timeFormat = LocalTime.Format {
    hour()
    char(':')
    minute()
}

class WilmeringBusCrawler(
    private val slowMode: Boolean = true,
) {

    suspend operator fun invoke(): List<Route> =
        getRoutesSites()
            .map { (slug, url) ->
                scrapeRoute(url, slug)
            }.also { println("Done crawling, got ${it.size} routes.") }

    private fun getRoutesSites(): List<Pair<RouteSlug, String>> {
        println("Fetch sitemap")
        val document = Jsoup.connect(siteMapUrl).parser(Parser.xmlParser()).get()
        return document.select("loc").map {
            val url = it.text()
            val slug = url.removeSuffix("/").split("/").last().asSlug()
            slug to url
        }.also { println("Got sitemap: $it") }
    }

    private suspend fun scrapeRoute(url: String, slug: RouteSlug): Route {
        pause()
        println("Scrape route $url")
        val document = Jsoup.connect(url).get()
        var arrivalStationSlug: MapEntitySlug? = null

        return Route(
            slug = slug,
            name = document.select(".wp-block-query-title").text().trim(),
            operator = wilmering,
            additionalInfo = mapOf(),
            ticketWebsites = listOf(),
            stations = document.select(".wp-block-buttons a")
                .map {
                    val (station, arrivalStation) = scrapeStation(url = it.attribute("href").value, slug)
                    arrivalStation?.let { arrivalStationSlug = it }
                    station
                },
            arrivalStationSlug = arrivalStationSlug!!,
        )
    }

    data class ScrapeStationResult(val station: Station, val arrivalStationSlug: MapEntitySlug?)

    private suspend fun scrapeStation(url: String, routeSlug: RouteSlug): ScrapeStationResult {
        pause()
        println("Scrape station $url")
        val document = Jsoup.connect(url).get()
        val title = document.select("h1")[1].text()
        return ScrapeStationResult(
            Station(
                slug = routeSlug + "_" + url.removePrefix("https://wilmering-stoma.de/haltestelle/").removeSuffix("/"),
                name = title,
                isNew = false,
                outward = document.select(".haltestelle_fahrten")
                    .map {
                        parseDepartureDay(it, "fahrten--hinfahrten")
                    },
                returns = document.select(".haltestelle_fahrten")
                    .map {
                        parseDepartureDay(it, "fahrten--rueckfahrten")
                    },
                location = null,
                prices = document.select(".haltestellen_infos__fahrpreise dl").first()!!.children()
                    .chunked(2) {
                        Fee(name = mapOf(de to it[0].text()), price = parsePrice(it[1].text()))
                    },
                ticketWebsites = listOf()
            ),
            document.select(".fahrten--rueckfahrten .fahrten__list__row__abfahrt p").first()?.text()
                ?.let {
                    when {
                        it.contains("Hof Gisela") -> TransportMapEntitySlugs.busbahnhofWest
                        it.contains("Busbahnhof") -> TransportMapEntitySlugs.busbahnhofOst
                        it.contains("Oldenburger Str.") -> TransportMapEntitySlugs.bushaltestelleOldenburgerstr
                        else -> throw IllegalArgumentException("Unknown station $it")
                    }
                }
        )
    }

    private fun parseDepartureDay(container: Element, className: String): DepartureDay {
        val day = dateFormat.parse(container.select("h3").text().split(",")[1].trim())
        return DepartureDay(
            day = day,
            departures = container.select(".$className").map {
                Departure(
                    time = parseTime(it.select(".fahrten__list__row__abfahrt h4").first()!!, day),
                    arrival = it.select(".fahrten__list__row__ankunft h4").first()?.let { parseTime(it, day) },
                )
            }.sortedBy { it.time },
            laterDepartureOnDemand = false
        )
    }

    private fun parseTime(element: Element, date: LocalDate): LocalDateTime =
        timeFormat.parse(timeRegex.find(element.text())!!.value).let {
            LocalDateTime(
                time = it,
                date = if (it.hour < firstHourOfNextDay) date.plus(DatePeriod(days = 1)) else date
            )
        }


    private fun parsePrice(priceText: String): Int {
        val (_, euro, cent) = priceRegex.find(priceText)?.groupValues
            ?: throw NumberFormatException("Can't parse price [$priceText]")
        return euro.toInt() * 100 + cent.toInt()
    }


    private suspend fun pause() {
        if (slowMode) {
            print("Waiting")
            delay(1.seconds)
            print(".")
            delay(1.seconds)
            print(".")
            delay(1.seconds)
            print(".")
            delay(1.seconds)
            print("\n")
        }
    }
}