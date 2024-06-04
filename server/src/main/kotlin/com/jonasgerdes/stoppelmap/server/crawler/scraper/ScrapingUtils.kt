package com.jonasgerdes.stoppelmap.server.crawler.scraper

import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.char

fun String.cleanPath() = removePrefix("/")

fun parseArticleSlugFromPath(url: String) =
    Regex("detail\\/([\\w-]+).*\$").find(url)?.groupValues?.getOrNull(1)

val articleOverviewDateFormat = LocalDate.Format {
    // "dd.MM.yyyy"
    dayOfMonth()
    char('.')
    monthNumber()
    char('.')
    year()
}