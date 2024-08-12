package com.jonasgerdes.stoppelmap.preparation.schedule.utils

import org.jsoup.Jsoup

fun String.htmlToText() =
    replace("<li>", "<li>\u2022 ")
        .let {
            Jsoup.parse(it)
                .wholeText()
                .lines()
                .map { it.trim() }
                .filter {
                    it.isNotBlank()
                }
                .joinToString(separator = "\n")
        }
        .ifBlank { null }