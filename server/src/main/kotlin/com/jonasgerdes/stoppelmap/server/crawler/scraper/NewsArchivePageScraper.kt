package com.jonasgerdes.stoppelmap.server.crawler.scraper

import com.jonasgerdes.stoppelmap.server.crawler.PageScraper
import com.jonasgerdes.stoppelmap.server.crawler.model.ArticlePreview
import com.jonasgerdes.stoppelmap.server.crawler.model.CrawlLogs
import org.jsoup.nodes.Document

class NewsArchivePageScraper(
    override val resourcePath: String
) : PageScraper<NewsArchivePageScraper.Content>() {

    constructor(page: Int) : this("service-infos/news-archiv/page-$page/")

    override fun parseDocument(document: Document, logs: CrawlLogs): Content = with(logs) {
        val articleElements = document.select(".newsarchive-list-item")
            .assert({ isNotEmpty() }) {
                error("No articles found (no elements match .newsarchive-list-item")
            }

        val articles = articleElements
            .mapNotNull { element ->
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
                    .selectElementOrError("time", "Missing article date")
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

        val pagination =
            document.selectElementOrError(".page-navigation > .pagination", "Missing pagination")

        val paginationUrls = pagination.selectElementsOrError("a", "No links in pagination")
            .mapNotNull {
                it.attribute("href").value.ifBlank { null }?.cleanPath()
            }

        Content(
            articles = articles,
            paginationUrls = paginationUrls,
        )
    }

    data class Content(
        val articles: List<ArticlePreview>,
        val paginationUrls: List<String>,
    )
}