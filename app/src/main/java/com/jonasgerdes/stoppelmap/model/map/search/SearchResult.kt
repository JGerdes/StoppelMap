package com.jonasgerdes.stoppelmap.model.map.search

import android.support.v7.util.DiffUtil
import com.jonasgerdes.stoppelmap.model.map.entity.Stall

sealed class SearchResult(val id: String) {

    data class SingleStallResult(
            val stall: Stall,
            val title: HighlightedText
    ) : SearchResult(stall.slug)

    object DiffCallback : DiffUtil.ItemCallback<SearchResult>() {
        override fun areItemsTheSame(oldItem: SearchResult, newItem: SearchResult) =
                oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: SearchResult, newItem: SearchResult) =
                oldItem.id == newItem.id

    }

}

data class Highlight(val start: Int, val length: Int) {
    val end = start + length
}

data class HighlightedText(val text: String, val highlights: List<Highlight>) {
    companion object {
        fun from(text: String, parts: List<String>): HighlightedText {
            val highlights = parts.foldRight(mutableListOf<Highlight>(), { part, highlights ->
                val start = text.indexOf(part, highlights.lastOrNull()?.end ?: 0, true)
                highlights.add(Highlight(start, part.length))
                highlights
            })
            return HighlightedText(text, highlights)
        }
    }
}