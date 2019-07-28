package com.jonasgerdes.stoppelmap.map.entity


data class HighlightedText(val text: String, val highlights: List<Highlight>) {
    companion object {
        fun withNoHighlights(text: String) = HighlightedText(text, emptyList())
        fun from(full: String, part: String) = from(full, listOf(part))
        fun from(full: String, parts: List<String>? = null): HighlightedText {
            val highlights = parts?.fold(mutableListOf(), { highlights, part ->
                val start = full.indexOf(part, (highlights.lastOrNull()?.end ?: 0), true)
                val element = Highlight(start, part.length)
                highlights.add(element)
                highlights
            }) ?: emptyList<Highlight>()
            return HighlightedText(full, highlights)
        }
    }

    data class Highlight(val start: Int, val length: Int) {
        val end = start + length
    }
}