package com.jonasgerdes.stoppelmap.server.crawler.scraper

import com.jonasgerdes.stoppelmap.server.crawler.PageScraper
import com.jonasgerdes.stoppelmap.server.crawler.model.CrawlLogs
import org.jsoup.nodes.Document

class ArticlePageScraper(override val resourcePath: String) :
    PageScraper<ArticlePageScraper.Content>() {

    override fun parseDocument(document: Document, logs: CrawlLogs): Content = with(logs) {
        // TODO: parse
        Content()
    }

    class Content
}