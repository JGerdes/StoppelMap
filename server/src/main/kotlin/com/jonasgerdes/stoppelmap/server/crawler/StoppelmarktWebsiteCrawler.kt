package com.jonasgerdes.stoppelmap.server.crawler

import com.jonasgerdes.stoppelmap.server.crawler.model.ArticlePreview
import com.jonasgerdes.stoppelmap.server.crawler.model.CrawlResult
import com.jonasgerdes.stoppelmap.server.crawler.model.CrawlerConfig
import com.jonasgerdes.stoppelmap.server.crawler.scraper.ArticlePageScraper
import com.jonasgerdes.stoppelmap.server.crawler.scraper.NewsArchivePageScraper
import com.jonasgerdes.stoppelmap.server.crawler.scraper.NewsPageScraper
import com.jonasgerdes.stoppelmap.server.news.data.Article
import com.jonasgerdes.stoppelmap.server.news.data.ArticleRepository
import com.jonasgerdes.stoppelmap.server.news.data.Image
import com.jonasgerdes.stoppelmap.server.news.data.ImageRepository
import com.jonasgerdes.stoppelmap.server.scheduler.ClockProvider
import com.jonasgerdes.stoppelmap.server.util.ULID
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.slf4j.Logger
import java.io.File
import kotlin.time.Duration.Companion.seconds

private val slowModeDelay = 5.seconds

class StoppelmarktWebsiteCrawler(
    private val crawlerConfig: CrawlerConfig,
    private val imageProcessor: ImageProcessor,
    private val articleRepository: ArticleRepository,
    private val imageRepository: ImageRepository,
    private val clockProvider: ClockProvider,
    private val logger: Logger,
) {

    enum class Mode {
        All,
        Latest,
    }

    suspend fun crawlNews(mode: Mode = Mode.Latest) {
        logger.info("Crawling news from ${crawlerConfig.baseUrl}${if (crawlerConfig.slowMode) " in slow mode" else ""}")
        val articlePreviews = mutableListOf<ArticlePreview>()
        when (val result = NewsPageScraper().invoke(crawlerConfig)) {
            is CrawlResult.Error -> {
                result.logs.logTo(logger)
            }

            is CrawlResult.Success -> articlePreviews.addAll(result.data.articles)
        }

        val archivePagesToScrape = ArrayDeque<String>().also {
            it.add(NewsArchivePageScraper(page = 1).resourcePath)
        }

        val pageIterator = archivePagesToScrape.iterator()
        while (pageIterator.hasNext()) {
            if (crawlerConfig.slowMode) delay(slowModeDelay)
            val resource = pageIterator.next()
            logger.debug("📥 Scraping article $resource")
            when (val result = NewsArchivePageScraper(resource).invoke(crawlerConfig)) {
                is CrawlResult.Error -> result.logs.logTo(logger)
                is CrawlResult.Success -> {
                    if (mode == Mode.All) {
                        result.data.paginationUrls.forEach {
                            if (!archivePagesToScrape.contains(it)) archivePagesToScrape.add(it)
                        }
                    }
                    articlePreviews.addAll(result.data.articles)
                }
            }
        }

        val scrapedArticles = articlePreviews.mapNotNull {
            if (crawlerConfig.slowMode) delay(slowModeDelay)
            logger.debug("Scraped article preview $it, getting full article")
            when (val result = ArticlePageScraper(preview = it).invoke(crawlerConfig)) {
                is CrawlResult.Error -> {
                    result.logs.logTo(logger)
                    null
                }

                is CrawlResult.Success -> result.data
            }
        }

        // Note: Articles only come with a publish date, without time.
        // To ensure we sort them in the correct order, we count how many articles we already have
        // for the same day and take that count to set some fake minutes/seconds of the day.
        // To not confuse this calculated datetime as a real one, we store the real publish date
        // and a ULID (timestamp based UUID), which we can use as a sort key.
        // Splitting up the count to minutes and seconds limits us to a total of 3600 articles
        // per day, but that should be more then enough.
        val existingPublishDates = articleRepository.getExistingPublishDates().toMutableMap()
        articleRepository.upsertAll(
            scrapedArticles
                .map {
                    val count = existingPublishDates.getOrDefault(it.publishDate, 0L)
                    existingPublishDates[it.publishDate] = count + 1
                    val sortKey = ULID.random(
                        LocalDateTime(
                            it.publishDate,
                            LocalTime(0, count.toInt() / 60, count.toInt() % 60, 0)
                        ).toInstant(TimeZone.UTC)
                    )
                    Article(
                        slug = it.slug,
                        title = it.title,
                        description = it.description,
                        publishedOn = it.publishDate,
                        content = it.content,
                        sortKey = sortKey,
                        createdAt = clockProvider.now(),
                        modifiedAt = clockProvider.now(),
                        isVisible = true
                    )
                }
        )

        val images = scrapedArticles.map { article ->
            article.images.mapNotNull { image ->
                if (crawlerConfig.slowMode) delay(slowModeDelay)
                val imageUrl = sanitizeUrl(image.url)
                when (val result = imageProcessor(
                    url = imageUrl,
                    articleSlug = article.slug,
                )) {
                    is ImageProcessor.Result.Error -> {
                        logger.warn(
                            "Failed to process image ${image.url}",
                            result.throwable
                        )
                        null
                    }

                    is ImageProcessor.Result.Success -> Image(
                        uuid = result.uuid,
                        articleSlug = article.slug,
                        caption = image.caption,
                        copyright = image.copyright,
                        blurHash = result.blurHash,
                        originalUrl = imageUrl,
                        originalFile = result.originalFile.parent + File.separator + result.originalFile.name,
                        processedFile = result.processedFile.name
                    )
                }
            }
        }.flatten()

        imageRepository.upsertAll(images)

        logger.info("Done crawling - saved ${scrapedArticles.size} articles and ${images.size} images")
    }

    private fun sanitizeUrl(url: String) =
        when {
            url.startsWith("http") -> url
            else -> crawlerConfig.baseUrl + url.removePrefix("/")
        }

}