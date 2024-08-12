package com.jonasgerdes.stoppelmap.map.model

data class FullMapEntity(
    val slug: String,
    val name: String?,
    val location: Location,
    val bounds: BoundingBox,
)
