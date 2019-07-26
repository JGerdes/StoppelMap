package com.jonasgerdes.stoppelmap.map.entity

import com.jonasgerdes.stoppelmap.model.map.Stall


sealed class SearchResult {
    abstract val title: HighlightedText
    abstract val subtitle: HighlightedText?
    abstract val score: Float

    data class StallSearchResult(
        override val title: HighlightedText,
        override val subtitle: HighlightedText? = null,
        override val score: Float = 1f,
        val stall: Stall
    ) : SearchResult()
}