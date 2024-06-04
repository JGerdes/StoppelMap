package com.jonasgerdes.stoppelmap.server.crawler.scraper

import com.jonasgerdes.stoppelmap.server.crawler.PageScraper
import com.jonasgerdes.stoppelmap.server.crawler.model.ArticlePreview
import com.jonasgerdes.stoppelmap.server.crawler.model.CrawlLogs
import com.jonasgerdes.stoppelmap.server.crawler.model.ScrapedArticle
import com.jonasgerdes.stoppelmap.server.crawler.model.ScrapedImage
import org.jsoup.nodes.Document

class ArticlePageScraper(private val preview: ArticlePreview) :
    PageScraper<ScrapedArticle>() {
    override val resourcePath = "aktuelles/detail/${preview.slug}/"

    override fun parseDocument(document: Document, logs: CrawlLogs) = with(logs) {
        val paragraphs =
            document.selectElementsOrError(".news-text-wrap > p", "No paragraphs found")
        val content = paragraphs.joinToString("\n\n") { it.text().trim() }
        val copyrights = extractCopyrights(content)

        val images = mutableListOf<ScrapedImage>()

        images += document.select("[itemprop=articleBody] img").mapIndexed { index, img ->
            val url = img.attr("src").trim()
            if (url.isNotBlank()) {
                ScrapedImage(
                    url = url,
                    author = copyrights.getOrNull(index)?.trim(),
                )
            } else null
        }.filterNotNull()

        val thumbnails = document.select(".thumbnail")

        images += thumbnails.mapNotNull { thumb ->
            val url = thumb.select("img").attr("src").trim()
            if (url.isNotBlank()) {
                val copyright = extractCopyrights(thumb.select(".caption").text())
                ScrapedImage(
                    url = url,
                    author = copyright.firstOrNull()?.trim(),
                    caption = thumb.select(".caption").text().trim()
                )
            } else null
        }

        ScrapedArticle(
            slug = preview.slug,
            title = preview.title,
            content = content,
            images = images.toList(),
            description = preview.description,
            publishDate = preview.publishDate
        )
    }


    private val copyrightRegex =
        Regex("\\(?(?:[Ff]otos?|[Pp]hotos?|[Bb]ilde?r?|©): ?(.{1,32}?)(?:\\)|\\n|\\Z)")

    private fun extractCopyrights(content: String) =
        copyrightRegex.find(content)?.groupValues?.drop(1) ?: emptyList()
}