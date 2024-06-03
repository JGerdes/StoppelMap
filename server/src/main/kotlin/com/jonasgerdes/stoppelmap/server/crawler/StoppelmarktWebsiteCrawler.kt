package com.jonasgerdes.stoppelmap.server.crawler

import com.jonasgerdes.stoppelmap.server.crawler.model.CrawlResult
import com.jonasgerdes.stoppelmap.server.crawler.model.CrawlerConfig
import com.jonasgerdes.stoppelmap.server.crawler.scraper.NewsArchivePageScraper
import com.jonasgerdes.stoppelmap.server.crawler.scraper.NewsPageScraper
import kotlinx.coroutines.delay
import org.slf4j.Logger
import kotlin.time.Duration.Companion.seconds

private val slowModeDelay = 5.seconds

class StoppelmarktWebsiteCrawler(
    private val baseUrl: String,
    version: String,
    private val logger: Logger,
    private val slowMode: Boolean,
) {
    private val crawlerConfig = CrawlerConfig(
        baseUrl = baseUrl,
        userAgent = "StoppelBot/$version (https://stoppelmap.de/bot)",
    )

    suspend fun crawlNews() {
        logger.info("Crawling news from $baseUrl${if (slowMode) " in slow mode" else ""}")
        val homeArticles = when (val result = NewsPageScraper().invoke(crawlerConfig)) {
            is CrawlResult.Error -> {
                result.logs.logTo(logger)
                emptyList()
            }

            is CrawlResult.Success -> result.data.articles
        }

        val archiveArticles = mutableSetOf<NewsArchivePageScraper.Content.Article>()
        val archivePagesToScrape = ArrayDeque<String>().also {
            it.add(NewsArchivePageScraper(page = 1).resourcePath)
        }

        val pageIterator = archivePagesToScrape.iterator()
        while (pageIterator.hasNext()) {
            if (slowMode) delay(slowModeDelay)
            val resource = pageIterator.next()
            logger.debug("📥 Scraping article $resource")
            when (val result = NewsArchivePageScraper(resource).invoke(crawlerConfig)) {
                is CrawlResult.Error -> result.logs.logTo(logger)
                is CrawlResult.Success -> {
                    result.data.paginationUrls.forEach {
                        if (!archivePagesToScrape.contains(it)) archivePagesToScrape.add(it)
                    }
                    archiveArticles.addAll(result.data.articles)
                }
            }
        }

        val allUrls = (
                homeArticles.map { it.fullArticleUrl } + archiveArticles.map { it.fullArticleUrl }
                ).toSet()
        logger.info("Crawled ${allUrls.size} articles")
    }
}