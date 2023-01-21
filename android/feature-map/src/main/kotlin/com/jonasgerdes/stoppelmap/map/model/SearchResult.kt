package com.jonasgerdes.stoppelmap.map.model

import com.jonasgerdes.stoppelmap.data.Stall

data class SearchResult(
    val term: String,
    val stalls: List<Stall>
)
