package com.jonasgerdes.stoppelmap.server.crawler.tasks

import com.jonasgerdes.stoppelmap.server.crawler.StoppelmarktWebsiteCrawler
import com.jonasgerdes.stoppelmap.server.scheduler.Schedule
import com.jonasgerdes.stoppelmap.server.scheduler.Task

class CrawlLatestNewsTask(
    private val stoppelmarktWebsiteCrawler: StoppelmarktWebsiteCrawler,
) : Task {

    override val schedule = Schedule.Hourly()

    override suspend fun invoke() {
        stoppelmarktWebsiteCrawler.crawlNews(mode = StoppelmarktWebsiteCrawler.Mode.Latest)
    }
}