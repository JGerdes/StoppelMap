package com.jonasgerdes.stoppelmap.server.crawler

import com.jonasgerdes.stoppelmap.server.crawler.model.CrawlLogs
import com.jonasgerdes.stoppelmap.server.crawler.model.CrawlResult
import com.jonasgerdes.stoppelmap.server.crawler.model.NewsPage
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime.Formats
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.toLocalDateTime
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.io.PrintWriter
import java.io.StringWriter

class StoppelmarktWebsiteCrawler(
    private val baseUrl: String,
    version: String
) {

    private val userAgent = "StoppelBot/$version (http://stoppelmap.de/bot)"

    companion object {
        const val pathNews = "aktuelles/"
        const val pathNewsArchive = "service-infos/news-archiv/"
    }

    fun crawlNewsPage(): CrawlResult<NewsPage> =
        downloadResourceAndParse(resource = baseUrl + pathNews) { document ->
            val articleList = document.select(".news-list-item")
                .first()
                .assertNotNull("Missing article list (no .news-list-item) in document")

            val articles = articleList.children().also {
                if (it.size != 2) warn("Unexpected number of articles: expected 2, got ${it.size}")
            }.map { element ->
                val headline = element
                    .selectElementOrError("h3", "Missing article headline")
                    .text()
                    .assert({ isNotBlank() }) { warn("Headline is empty") }

                val teaser = element
                    .selectElementOrError(".lead", "Missing article teaser")
                    .text()
                    .assert({ isNotBlank() }) { warn("Teaser is empty") }

                val url = element
                    .selectElementOrError(".btn-read-more", "Missing article read more button")
                    .attribute("href").value
                    .assert({ isNotBlank() }) { warn("Article url is empty") }

                NewsPage.Article(
                    headline = headline,
                    teaser = teaser,
                    fullArticleUrl = url
                )

            }

            NewsPage(articles = articles)
        }

    private fun <T> downloadResourceAndParse(
        resource: String,
        title: String? = null,
        parser: CrawlLogs.(Document) -> T
    ): CrawlResult<T> {
        val logs = CrawlLogs(resource = resource, title = title)
        val start = Clock.System.now()
        with(logs) {
            info(
                "Start (UTC): ${
                    start.toLocalDateTime(TimeZone.UTC).format(Formats.ISO)
                }"
            )
            title?.let { info("Title: $title") }
            info("Resource: $resource")
            info("User-Agent: $userAgent")
            pending("Jsoup connect and fetch")
        }
        val document = try {
            Jsoup.connect(resource)
                .userAgent(userAgent)
                .get()
        } catch (t: Throwable) {
            val done = Clock.System.now()
            logs.info(
                "Done (UTC): ${
                    done.toLocalDateTime(TimeZone.UTC).format(Formats.ISO)
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
        logs.pending("Parsing document")
        return try {
            val result = logs.parser(document)
            val done = Clock.System.now()
            logs.info(
                "Done (UTC): ${
                    done.toLocalDateTime(TimeZone.UTC).format(Formats.ISO)
                }"
            )
            logs.info(
                "Duration: ${done - start}"
            )
            logs.check("Done. Result: $result")
            if (logs.logs.hasErrorOrWarning()) logs.attach(document.outerHtml())
            CrawlResult.Success(result, logs)
        } catch (t: Throwable) {
            val done = Clock.System.now()
            logs.info(
                "Done (UTC): ${
                    done.toLocalDateTime(TimeZone.UTC).format(Formats.ISO)
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
}

private fun Element.selectElementOrError(selector: String, message: String): Element =
    select(selector).first().assertNotNull("$message (no $selector in ${cssSelector()})")

private fun <T> T?.assertNotNull(message: String): T {
    if (this == null) throw ParsingException(message)
    else return this
}

private fun <T> T.assert(assertion: T.() -> Boolean, logAction: () -> Any): T {
    if (!assertion()) logAction()
    return this
}

class ParsingException(override val message: String) : Throwable()