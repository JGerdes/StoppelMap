package com.jonasgerdes.stoppelmap.server.crawler.tasks

import com.jonasgerdes.stoppelmap.server.crawler.StoppelmarktWebsiteCrawler
import com.jonasgerdes.stoppelmap.server.scheduler.Schedule
import com.jonasgerdes.stoppelmap.server.scheduler.Task

class CrawlLatestNewsTask(
    private val stoppelmarktWebsiteCrawler: StoppelmarktWebsiteCrawler,
    override val schedule: Schedule,
) : Task {

    override suspend fun invoke() {
        stoppelmarktWebsiteCrawler.crawlNews(mode = StoppelmarktWebsiteCrawler.Mode.Latest)
    }
}