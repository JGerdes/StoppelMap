package com.jonasgerdes.stoppelmap.server.monitoring

import com.jonasgerdes.stoppelmap.server.news.data.ArticleRepository
import com.jonasgerdes.stoppelmap.server.scheduler.Schedule
import com.jonasgerdes.stoppelmap.server.scheduler.Task

class InitMetricsTask(
    val articleRepository: ArticleRepository,
    val monitoring: Monitoring,
) : Task {
    override val name: String = "InitMetrics"
    override val schedule: Schedule = Schedule.Once

    override suspend fun invoke() {
        with(monitoring) {
            newsCounter?.set(articleRepository.getCount())
        }
    }
}