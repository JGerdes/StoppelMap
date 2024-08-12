package com.jonasgerdes.stoppelmap.map.model

data class FullMapEntity(
    val slug: String,
    val name: String,
    val type: String?,
    val subType: String?,
    val description: String?,
    val location: Location,
    val bounds: BoundingBox,
    val icon: MapIcon,
)
