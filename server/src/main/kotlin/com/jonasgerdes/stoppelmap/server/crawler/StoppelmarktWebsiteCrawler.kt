package com.jonasgerdes.stoppelmap.server.crawler

import com.jonasgerdes.stoppelmap.server.crawler.model.ArticlePreview
import com.jonasgerdes.stoppelmap.server.crawler.model.CrawlResult
import com.jonasgerdes.stoppelmap.server.crawler.model.CrawlerConfig
import com.jonasgerdes.stoppelmap.server.crawler.model.Image
import com.jonasgerdes.stoppelmap.server.crawler.scraper.ArticlePageScraper
import com.jonasgerdes.stoppelmap.server.crawler.scraper.NewsArchivePageScraper
import com.jonasgerdes.stoppelmap.server.crawler.scraper.NewsPageScraper
import com.jonasgerdes.stoppelmap.server.data.ArticleRepository
import com.jonasgerdes.stoppelmap.server.news.Article
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import org.slf4j.Logger
import kotlin.time.Duration.Companion.seconds

private val slowModeDelay = 5.seconds

class StoppelmarktWebsiteCrawler(
    private val crawlerConfig: CrawlerConfig,
    private val imageProcessor: ImageProcessor,
    private val articleRepository: ArticleRepository,
    private val logger: Logger,
) {
    suspend fun crawlNews() {
        logger.info("Crawling news from ${crawlerConfig.baseUrl}${if (crawlerConfig.slowMode) " in slow mode" else ""}")
        val articlePreviews = mutableSetOf<ArticlePreview>()
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
                    result.data.paginationUrls.forEach {
                        if (!archivePagesToScrape.contains(it)) archivePagesToScrape.add(it)
                    }
                    articlePreviews.addAll(result.data.articles)
                }
            }
        }

        val scrapedArticles = articlePreviews.mapNotNull {
            logger.debug("Scraped article preview $it, getting full article")
            when (val result = ArticlePageScraper(preview = it).invoke(crawlerConfig)) {
                is CrawlResult.Error -> {
                    result.logs.logTo(logger)
                    null
                }

                is CrawlResult.Success -> result.data
            }
        }

        articleRepository.upsertAll(
            scrapedArticles.map {
                Article(
                    slug = it.slug,
                    title = it.title,
                    description = it.description,
                    publishedOn = it.publishDate,
                    content = it.content,
                    createdAt = Clock.System.now(),
                    isVisible = true
                )
            }
        )

        /*val fullArticles = scrapedArticles.map { article ->
            article.toFullArticle(
                images = article.images.mapNotNull { image ->
                    if (crawlerConfig.slowMode) delay(slowModeDelay)
                    when (val result = imageProcessor(
                        baseUrl = crawlerConfig.baseUrl,
                        imageUrl = image.url,
                        articleSlug = article.slug,
                    )) {
                        is ImageProcessor.Result.Error -> {
                            logger.warn(
                                "Failed to process image ${image.url}",
                                result.throwable
                            )
                            null
                        }

                        is ImageProcessor.Result.Success -> {
                            Image(
                                url = image.url,
                                caption = image.caption,
                                author = image.author,
                                localFile = result.localFile,
                                blurHash = result.blurHash
                            )
                        }
                    }
                }
            )
        }
        fullArticles.forEach {
            logger.debug("Article ${it.slug} from ${it.publishDate} ${it.description} with images ${it.images}")
        }
        logger.info("Crawled ${fullArticles.size} articles (${articlePreviews.size} previews)")
    }
}