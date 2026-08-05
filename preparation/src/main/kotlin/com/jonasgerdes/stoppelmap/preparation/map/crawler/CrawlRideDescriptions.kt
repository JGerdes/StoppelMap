package com.jonasgerdes.stoppelmap.preparation.map.crawler

import com.jonasgerdes.stoppelmap.preparation.Settings
import com.jonasgerdes.stoppelmap.preparation.preparationModule
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import java.io.File

@OptIn(ExperimentalSerializationApi::class)
class CrawlRideDescriptions : KoinComponent {

    private val settings: Settings by inject()
    private val crawler by lazy { StoppelmarktDescriptionCrawler() }

    operator fun invoke() {
        println("Crawling ride descriptions")
        val descriptionFolder = settings.descriptionFolder.apply { mkdirs() }
        val shortDescriptionFolder = settings.shortDescriptionFolder.apply { mkdirs() }
        crawler().forEach { description ->
            if (description.description != null) {
                val file = File(descriptionFolder, "${description.slug}.html")
                println("Write description of [${description.slug}] to $file (url was ${description.url})")
                file.writeText(description.description)
            }
            if (description.shortDescription != null) {
                val file = File(shortDescriptionFolder, "${description.slug}.html")
                println("Write short description of [${description.slug}] to $file (url was ${description.url})")
                file.writeText(description.shortDescription)
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

    runBlocking {
        CrawlRideDescriptions().invoke()
    }
}