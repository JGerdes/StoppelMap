package com.jonasgerdes.stoppelmap.model.map.search

import com.jonasgerdes.stoppelmap.model.map.entity.Stall

sealed class SearchResult(val id: String) {

    data class SingleStallResult(
            val stall: Stall,
            val title: HighlightedText,
            val subtitle: HighlightedText? = null
    ) : SearchResult(stall.slug)

}

data class Highlight(val start: Int, val length: Int) {
    val end = start + length
}

data class HighlightedText(val text: String, val highlights: List<Highlight>) {
    companion object {
        fun from(text: String, parts: List<String>? = null): HighlightedText {
            val highlights = parts?.fold(mutableListOf(), { highlights, part ->
                val start = text.indexOf(part, (highlights.lastOrNull()?.end ?: 0), true)
                val element = Highlight(start, part.length)
                highlights.add(element)
                highlights
            }) ?: emptyList<Highlight>()
            return HighlightedText(text, highlights)
        }
    }
}