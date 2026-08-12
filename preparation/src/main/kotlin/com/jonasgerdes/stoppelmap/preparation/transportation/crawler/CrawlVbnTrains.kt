package com.jonasgerdes.stoppelmap.preparation.transportation.crawler

import com.jonasgerdes.stoppelmap.preparation.Settings
import com.jonasgerdes.stoppelmap.preparation.preparationModule
import com.jonasgerdes.stoppelmap.preparation.util.calculateDatesForYear
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToStream
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import java.io.File

@OptIn(ExperimentalSerializationApi::class)
class CrawlVbnTrains : KoinComponent {

    private val settings: Settings by inject()

    operator fun invoke() {
        val tempDir = File("train-temp").also { it.mkdirs() }
        val seasonDates = calculateDatesForYear(settings.year)
        if (tempDir.list().isEmpty()) {
            val crawler = VbnCrawler(
                slowMode = true,
                hourStep = 1,
                folder = tempDir
            )

            runBlocking {
                crawler(
                    dates = seasonDates
                )
            }
        }

        val routes = TrainJourneyProcessor().invoke(tempDir, seasonDates = seasonDates)

        val json = Json {
            prettyPrint = true
            prettyPrintIndent = "  "
            explicitNulls = false
            encodeDefaults = false
        }

        routes.forEach { route ->
            File(settings.crawledTrainRoutesDirectory, "${route.slug}.json").outputStream().buffered().use { stream ->
                json.encodeToStream(route, stream)
            }
        }
    }
}

fun main(args: Array<String>) {
    startKoin {
        modules(
            preparationModule
        )
    }

    CrawlVbnTrains().invoke()
}