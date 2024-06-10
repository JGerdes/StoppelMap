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
        var globalCopyright: String? = copyrights.firstOrNull { it.useForAllImages }?.copyright

        val images = mutableListOf<ScrapedImage>()

        images += document.select("[itemprop=articleBody] img").mapIndexed { index, img ->
            val url = img.attr("src").trim()
            if (url.isNotBlank()) {
                ScrapedImage(
                    url = url,
                    copyright = copyrights.getOrNull(index)?.copyright,
                )
            } else null
        }.filterNotNull()

        val thumbnails = document.select(".thumbnail")

        images += thumbnails.mapNotNull { thumb ->
            val url = thumb.select("img").attr("src").trim()
            if (url.isNotBlank()) {
                val caption = thumb.select(".caption").text().trim()
                val copyright = extractCopyrights(caption).firstOrNull()
                if (copyright?.useForAllImages == true && globalCopyright == null) {
                    globalCopyright = copyright.copyright
                }
                ScrapedImage(
                    url = url,
                    copyright = copyright?.copyright,
                    caption = caption
                )
            } else null
        }

        val finalImages = globalCopyright?.let { globalCopyright ->
            images.map {
                if (it.copyright != null) it else it.copy(copyright = globalCopyright)
            }
        } ?: images.toList()

        ScrapedArticle(
            slug = preview.slug,
            title = preview.title,
            content = content,
            images = finalImages,
            description = preview.description,
            publishDate = preview.publishDate
        )
    }


    private val copyrightRegex =
        Regex("\\(?(?:[Ff]otos?|[Pp]hotos?|[Bb]ilde?r?|©): ?©? ?(.{1,64}?)(?: ?im Auftrag der Stadt Vechta)?(?: ?\\(?honorarfrei\\)?)?(?:\\)|\\n|\\Z)")
    private val pluralCopyrightRegex = Regex("[Ff]otos|[Pp]hotos|[Bb]ilder")

    private fun extractCopyrights(content: String): List<Copyright> {
        val resultsGroups = copyrightRegex.find(content)?.groupValues ?: return emptyList()
        return resultsGroups.drop(1).map {
            Copyright(
                copyright = it.trim(),
                useForAllImages = resultsGroups.firstOrNull()?.matches(copyrightRegex) == true
            )
        }
    }

    private data class Copyright(val copyright: String, val useForAllImages: Boolean)
}