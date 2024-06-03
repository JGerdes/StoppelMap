package com.jonasgerdes.stoppelmap.server.crawler.scraper

import com.jonasgerdes.stoppelmap.server.crawler.PageScraper
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

            Content.Article(
                headline = headline,
                teaser = teaser,
                fullArticleUrl = url.cleanPath()
            )

        }

        Content(articles = articles)
    }

    data class Content(
        val articles: List<Article>
    ) {
        data class Article(
            val headline: String,
            val teaser: String,
            val fullArticleUrl: String,
        )
    }
}