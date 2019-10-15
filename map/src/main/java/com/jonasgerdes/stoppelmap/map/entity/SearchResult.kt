package com.jonasgerdes.stoppelmap.map.entity

import com.jonasgerdes.stoppelmap.model.map.Item
import com.jonasgerdes.stoppelmap.model.map.Stall
import com.jonasgerdes.stoppelmap.model.map.SubType


sealed class SearchResult {
    abstract val title: HighlightedText
    abstract val subtitle: HighlightedText?
    abstract val score: Float

    abstract val stallSlugs: List<String>

    data class StallSearchResult(
        override val title: HighlightedText,
        override val subtitle: HighlightedText? = null,
        override val score: Float = 1f,
        val stall: Stall
    ) : SearchResult() {
        override val stallSlugs get() = listOf(stall.slug)
    }

    data class TypeSearchResult(
        override val title: HighlightedText,
        override val subtitle: HighlightedText? = null,
        override val score: Float = 1f,
        val stalls: List<Stall>,
        val type: SubType
    ) : SearchResult() {
        override val stallSlugs get() = stalls.map { it.slug }
    }

    data class ItemSearchResult(
        override val title: HighlightedText,
        override val subtitle: HighlightedText? = null,
        override val score: Float = 1f,
        val stalls: List<Stall>,
        val item: Item
    ) : SearchResult() {
        override val stallSlugs get() = stalls.map { it.slug }
    }
}