package com.jonasgerdes.stoppelmap.preparation.map.crawler

import com.jonasgerdes.stoppelmap.preparation.schedule.utils.htmlToText
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.jsoup.Jsoup
import kotlin.time.Duration.Companion.seconds

const val baseUrl = "https://www.stoppelmarkt.de"
const val overviewUrl = "${baseUrl}/attraktionen/"

data class RideDescription(
    val slug: String,
    val url: String,
    val shortDescription: String?,
    val description: String? = null,
)

class StoppelmarktDescriptionCrawler(
    private val slowMode: Boolean = true,
) {

    operator fun invoke(): Sequence<RideDescription> =
        crawlOverview(overviewUrl.also { println("Fetching $it") })
            .also { println("Found ${it.size} results, iterating") }
            .asSequence()
            .map {
                runBlocking { pause() }
                println("Fetching page for $it")
                fetchFullDescription(it)
            }


    private fun crawlOverview(url: String): List<RideDescription> {
        println("start parsing website for events")
        return Jsoup.connect(url).get().body()
            .select(".ce-textpic > .ce-bodytext")
            .toList()
            .filter { it.select("a").isNotEmpty() }
            .map {
                val locationUrl = it.select("a").attr("href")
                RideDescription(
                    slug = locationUrl
                        .removePrefix("https://www.stoppelmarkt.de/")
                        .removePrefix("/attraktionen")
                        .removePrefix("/")
                        .removeSuffix("/")
                        .removeSuffix(".html")
                        .removeSuffix("-1")
                        .removeSuffix("-2"),
                    url = locationUrl,
                    shortDescription = it.select("p").firstOrNull()?.html()?.htmlToText()
                )
            }
    }

    private fun fetchFullDescription(rideDescription: RideDescription): RideDescription {
        val detailBody = Jsoup.connect(baseUrl + rideDescription.url).get().body()
        val descriptionNodes = detailBody.select(".txtcnt p:not(.gm-style-mot)")
        val description = descriptionNodes.html().htmlToText()
        return rideDescription.copy(description = description)
    }


    private suspend fun pause() {
        if (slowMode) {
            print("Waiting")
            delay(1.seconds)
            print(".")
            delay(1.seconds)
            print(".")
            delay(1.seconds)
            print("\n")
        }
    }
}