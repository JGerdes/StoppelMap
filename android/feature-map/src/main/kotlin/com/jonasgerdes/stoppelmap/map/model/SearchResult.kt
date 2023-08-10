package com.jonasgerdes.stoppelmap.map.model

import com.jonasgerdes.stoppelmap.data.Stall

data class SearchResult(
    val term: String,
    val stalls: List<Stall>,
    val type: Type = Type.Search
) {
    enum class Type {
        Search, History
    }
}
