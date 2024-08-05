package com.jonasgerdes.stoppelmap.preparation.transportation.crawler

import com.jonasgerdes.stoppelmap.preparation.Settings
import com.jonasgerdes.stoppelmap.preparation.preparationModule
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToStream
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import java.io.File

@OptIn(ExperimentalSerializationApi::class)
class CrawlBusWebsites : KoinComponent {

    private val settings: Settings by inject()

    operator fun invoke() {
        val wilmeringBusCrawler = WilmeringBusCrawler()

        val busRoutes = runBlocking {
            wilmeringBusCrawler()
        }

        val json = Json {
            prettyPrint = true
            prettyPrintIndent = "  "
            explicitNulls = false
            encodeDefaults = false
        }

        busRoutes.forEach { route ->
            File(settings.crawledRoutesDirectory, "${route.slug}.json").outputStream().buffered().use { stream ->
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

    CrawlBusWebsites().invoke()
}