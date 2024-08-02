package com.jonasgerdes.stoppelmap.server.crawler.tasks

import com.jonasgerdes.stoppelmap.server.crawler.StoppelmarktWebsiteCrawler
import com.jonasgerdes.stoppelmap.server.news.data.ArticleRepository
import com.jonasgerdes.stoppelmap.server.scheduler.Schedule
import com.jonasgerdes.stoppelmap.server.scheduler.Task
import org.slf4j.Logger

class CrawlAllNewsInitialTask(
    private val articleRepository: ArticleRepository,
    private val stoppelmarktWebsiteCrawler: StoppelmarktWebsiteCrawler,
    private val logger: Logger,
) : Task {

    override val schedule = Schedule.Once
    override val name: String = "CrawlAllNewsInitial"

    override suspend fun invoke() {
        val count = articleRepository.getCount()
        if (count == 0) {
            logger.info("No articles yet, crawling all.")
            stoppelmarktWebsiteCrawler.crawlNews(mode = StoppelmarktWebsiteCrawler.Mode.All)
        } else {
            logger.info("Already have $count articles, no initial crawl.")
        }
    }
}