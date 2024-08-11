package com.jonasgerdes.stoppelmap.map.model

data class StallSummary(
    val slug: String,
    val name: String,
    val typeName: String?,
    val subTypeName: String?,
    val icon: MapIcon
)
