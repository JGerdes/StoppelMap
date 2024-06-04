package com.jonasgerdes.stoppelmap.server.crawler.scraper

import com.jonasgerdes.stoppelmap.server.crawler.PageScraper
import com.jonasgerdes.stoppelmap.server.crawler.model.ArticlePreview
import com.jonasgerdes.stoppelmap.server.crawler.model.CrawlLogs
import org.jsoup.nodes.Document

class NewsPageScraper : PageScraper<NewsPageScraper.Content>() {
    override val resourcePath = "aktuelles/"

    override fun parseDocument(document: Document, logs: CrawlLogs): Content = with(logs) {
        val articleList = document.select(".news-list-item")
            .first()
            .assertNotNull("Missing article list (no .news-list-item) in document")

        val articles = articleList.children().also {
            if (it.size != 2) warn("Unexpected number of articles: expected 2, got ${it.size}")
        }.mapNotNull { element ->
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

            val date = element
                .selectElementOrError(".date > time", "Missing article date")
                .attribute("datetime").value
                .ifBlank { null }
                .assertNotNull("Article date is empty")

            val slug = parseArticleSlugFromPath(url)
            if (slug == null) {
                warn("Error parsing slug ($url)")
            }

            slug?.let { slug ->
                ArticlePreview(
                    slug = slug,
                    title = headline,
                    description = teaser,
                    publishDate = articleOverviewDateFormat.parse(date)
                )
            }
        }

        Content(articles = articles)
    }

    data class Content(
        val articles: List<ArticlePreview>
    )
}