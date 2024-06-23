package com.jonasgerdes.stoppelmap.server.crawler

import com.jonasgerdes.stoppelmap.server.config.AppConfig
import com.jonasgerdes.stoppelmap.server.crawler.model.CrawlerConfig
import com.jonasgerdes.stoppelmap.server.crawler.tasks.CrawlAllNewsInitialTask
import com.jonasgerdes.stoppelmap.server.crawler.tasks.CrawlLatestNewsTask
import com.jonasgerdes.stoppelmap.server.scheduler.Schedule
import com.jonasgerdes.stoppelmap.server.scheduler.Task
import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.nio.ImageIOReader
import com.sksamuel.scrimage.webp.WebpImageReader
import com.sksamuel.scrimage.webp.WebpWriter
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.UserAgent
import org.koin.dsl.bind
import org.koin.dsl.module
import java.io.File

val crawlerModule = module {
    single {
        val appConfig = get<AppConfig>()
        CrawlerConfig(
            baseUrl = appConfig.crawler.baseUrl,
            userAgent = "StoppelBot/${appConfig.version} (https://stoppelmap.de/bot)",
            slowMode = appConfig.crawler.slowMode
        )
    }

    single {
        HttpClient(CIO) {
            install(UserAgent) {
                agent = get<CrawlerConfig>().userAgent
            }
        }
    }

    single {
        StoppelmarktWebsiteCrawler(
            crawlerConfig = get(),
            imageProcessor = get(),
            logger = get(),
            articleRepository = get(),
            imageRepository = get(),
            clockProvider = get(),
        )
    }

    single {
        ImageProcessor(
            httpClient = get(),
            imageLoader = ImmutableImage.loader()
                .withImageReaders(listOf(ImageIOReader(), WebpImageReader())),
            imageWriter = WebpWriter.DEFAULT
                .withQ(75)
                .withM(6)
                .withoutAlpha(),
            processedImageExtension = "webp",
            imageCacheDirectory = File(get<AppConfig>().crawler.imageCacheDir),
            logger = get()
        )
    }
}

fun crawlerTasksModule(crawlerConfig: AppConfig.Crawler) = module {
    if (crawlerConfig.doInitialFullCrawl) {
        single {
            CrawlAllNewsInitialTask(
                articleRepository = get(),
                stoppelmarktWebsiteCrawler = get(),
                logger = get(),
            )
        } bind Task::class
    }

    if (crawlerConfig.periodicCrawlHours.isNotEmpty()) {
        single {
            CrawlLatestNewsTask(
                stoppelmarktWebsiteCrawler = get(),
                schedule = Schedule.HoursOfDay(crawlerConfig.periodicCrawlHours)
            )
        } bind Task::class
    }
}