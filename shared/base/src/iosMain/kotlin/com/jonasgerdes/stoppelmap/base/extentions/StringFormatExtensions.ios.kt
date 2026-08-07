package com.jonasgerdes.stoppelmap.base.extentions

private val linkPattern by lazy { Regex("<a href=\"(.*?)\">(.*?)</a>") }

actual fun String.parseFormat(): String =
    replace("\n", "")
        .replace("<p>", "")
        .replace("</p>", "\n")
        .replace("<br>", "\n")
        .replace("<b>", "**")
        .replace("</b>", "**")
        .replace("<i>", "__")
        .replace("</i>", "__")
        .replace("&nbsp;", " ")
        .replace("&amp;", "&")
        .replace(linkPattern) { result ->
            "[${result.groupValues[2]}](${result.groupValues[1]})"
        }