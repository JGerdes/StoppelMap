package com.jonasgerdes.stoppelmap.preparation.schedule.utils

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.NodeFilter.FilterResult

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

private val allowedTags = setOf(
    "b",
    "i",
    "strong",
    "italic",
    "span",
    "p",
    "div",
    "br",
    "a"
)

private val emptySpace = setOf(
    "<p>&nbsp;</p>",
    "<p></p>",
    "<p> </p>",
    "<br>",
)

private val tagReplacements = setOf(
    "<strong>" to "<b>",
    "</strong>" to "</b>",
    "<italic>" to "<i>",
    "</italic>" to "</i>",
    "<span>" to "",
    "</span>" to "",
    "<div>" to "",
    "</div>" to "",
    "<br/>" to "<br>",
    "</br>" to "<br>",
)

fun String.cleanHtml(): String = Jsoup.parse(this)
    .body()
    .filter { node, i ->
        when {
            node.childNodeSize() == 0 -> FilterResult.SKIP_ENTIRELY
            (node as? Element)?.text()?.isBlank() == true -> FilterResult.SKIP_ENTIRELY
            allowedTags.any { node.nameIs(it) } -> FilterResult.CONTINUE
            else -> FilterResult.SKIP_ENTIRELY
        }
    }
    .html()
    .trim()
    .let {
        var text = it
        tagReplacements.forEach { (from, to) ->
            text = text.replace(from, to)
        }
        while (emptySpace.any { text.startsWith(it) }) {
            emptySpace.forEach {
                text = text.removePrefix(it)
            }
        }
        while (emptySpace.any { text.endsWith(it) }) {
            emptySpace.forEach {
                text = text.removeSuffix(it)
            }
        }
        text
    }

private val phoneRegex by lazy { Regex("(?:\\+49|0)(\\d{3,4}[\\/\\s]?\\d+)") }

fun String.addPhoneLinks(): String = replace(phoneRegex) { matchResult ->
    "<a href=\"tel:+49${
        matchResult.groupValues[1].replace(
            Regex("[\\/\\s]"),
            ""
        )
    }\">${matchResult.groupValues[0]}</a>"
}