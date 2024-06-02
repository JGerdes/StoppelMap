package com.jonasgerdes.stoppelmap.server.crawler.model

data class NewsPage(
    val articles: List<Article>
) {
    data class Article(
        val headline: String,
        val teaser: String,
        val fullArticleUrl: String,
    )
}
