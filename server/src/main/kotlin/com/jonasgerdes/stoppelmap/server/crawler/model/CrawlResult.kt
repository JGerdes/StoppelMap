package com.jonasgerdes.stoppelmap.server.crawler.model

sealed interface CrawlResult<T> {
    val logs: CrawlLogs

    data class Success<T>(val data: T, override val logs: CrawlLogs) : CrawlResult<T>
    data class Error<T>(
        override val logs: CrawlLogs,
        val throwable: Throwable? = null
    ) : CrawlResult<T>
}