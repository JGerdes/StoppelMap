package com.jonasgerdes.stoppelmap.server.crawler.tasks

import com.jonasgerdes.stoppelmap.server.crawler.StoppelmarktWebsiteCrawler
import com.jonasgerdes.stoppelmap.server.monitoring.Monitoring
import com.jonasgerdes.stoppelmap.server.monitoring.coRecord
import com.jonasgerdes.stoppelmap.server.scheduler.Schedule
import com.jonasgerdes.stoppelmap.server.scheduler.Task

class CrawlLatestNewsTask(
    private val stoppelmarktWebsiteCrawler: StoppelmarktWebsiteCrawler,
    override val schedule: Schedule,
    private val monitoring: Monitoring,
) : Task {

    override val name: String = "CrawLatestNews"

    override suspend fun invoke() {
        monitoring.newsCrawlLatestTimer.coRecord {
            stoppelmarktWebsiteCrawler.crawlNews(mode = StoppelmarktWebsiteCrawler.Mode.Latest)
        }
    }
}