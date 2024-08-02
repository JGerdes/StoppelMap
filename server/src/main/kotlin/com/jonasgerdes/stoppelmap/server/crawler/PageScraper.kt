package com.jonasgerdes.stoppelmap.server.crawler

import com.jonasgerdes.stoppelmap.server.crawler.model.CrawlLogs
import com.jonasgerdes.stoppelmap.server.crawler.model.CrawlResult
import com.jonasgerdes.stoppelmap.server.crawler.model.CrawlerConfig
import com.jonasgerdes.stoppelmap.server.monitoring.Monitoring
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.toLocalDateTime
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.io.PrintWriter
import java.io.StringWriter

abstract class PageScraper<T> {
    abstract val resourcePath: String
    protected val title: String? = null

    abstract fun parseDocument(document: Document, logs: CrawlLogs): T

    operator fun invoke(crawlerConfig: CrawlerConfig, monitoring: Monitoring): CrawlResult<T> {
        val resource = crawlerConfig.baseUrl + resourcePath
        val logs = CrawlLogs(resource = resource, title = title)
        val start = Clock.System.now()
        with(logs) {
            info(
                "Start (UTC): ${
                    start.toLocalDateTime(TimeZone.UTC).format(LocalDateTime.Formats.ISO)
                }"
            )
            title?.let { info("Title: $title") }
            info("Resource: $resource")
            info("User-Agent: ${crawlerConfig.userAgent}")
            pending("Jsoup connect and fetch")
        }
        val document = try {
            monitoring.newsFetchArticleTimer.recordCallable {
                Jsoup.connect(resource)
                    .userAgent(crawlerConfig.userAgent)
                    .get()
            }
        } catch (t: Throwable) {
            val done = Clock.System.now()
            logs.info(
                "Done (UTC): ${
                    done.toLocalDateTime(TimeZone.UTC).format(LocalDateTime.Formats.ISO)
                }"
            )
            logs.info(
                "Duration: ${done - start}"
            )
            logs.error("Error during download: ${t.message}")
            logs.error(
                StringWriter()
                    .also {
                        t.printStackTrace(PrintWriter(it))
                    }.toString()
            )
            return CrawlResult.Error(logs, t)
        }
        logs.check("Fetched resource")
        return try {
            logs.pending("Parsing document")
            val result = parseDocument(document, logs)
            val done = Clock.System.now()
            logs.info(
                "Done (UTC): ${
                    done.toLocalDateTime(TimeZone.UTC).format(LocalDateTime.Formats.ISO)
                }"
            )
            logs.info("Duration: ${done - start}")
            logs.check("Done. Result: $result")
            if (logs.logs.hasErrorOrWarning()) logs.attach(document.outerHtml())
            CrawlResult.Success(result, logs)
        } catch (t: Throwable) {
            val done = Clock.System.now()
            logs.info(
                "Done (UTC): ${
                    done.toLocalDateTime(TimeZone.UTC).format(LocalDateTime.Formats.ISO)
                }"
            )
            logs.info(
                "Duration: ${done - start}"
            )
            logs.error(t.message ?: "Exception occurred")
            if (t !is ParsingException) {
                logs.error(
                    StringWriter()
                        .also {
                            t.printStackTrace(PrintWriter(it))
                        }.toString()
                )
            }
            CrawlResult.Error(logs.attach(document.outerHtml()), t)
        }
    }

    protected fun Element.selectElementOrError(selector: String, message: String): Element =
        select(selector).first().assertNotNull("$message (no $selector in ${cssSelector()})")

    protected fun Element.selectElementsOrError(selector: String, message: String): Elements =
        select(selector).assertNotEmpty("$message (no $selector in ${cssSelector()})")

    protected fun Element.selectElementOr(selector: String, logAction: () -> Any): Element? =
        select(selector).first().also { if (it == null) logAction() }

    protected fun <T> T?.assertNotNull(message: String): T {
        if (this == null) throw ParsingException(message)
        else return this
    }

    protected fun <T : List<*>> T.assertNotEmpty(message: String): T {
        if (isEmpty()) throw ParsingException(message)
        else return this
    }

    protected fun <T> T.assert(assertion: T.() -> Boolean, logAction: () -> Any): T {
        if (!assertion()) logAction()
        return this
    }

    class ParsingException(override val message: String) : Throwable()
}