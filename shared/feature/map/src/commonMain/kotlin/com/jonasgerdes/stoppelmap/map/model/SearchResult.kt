package com.jonasgerdes.stoppelmap.map.model

data class SearchResult(
    val term: String,
    val icon: MapIcon?,
    val score: Float,
    val resultEntities: List<StallSummary>,
    val type: Type
) {
    enum class Type {
        SingleStall
    }
}
