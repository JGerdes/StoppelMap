package com.jonasgerdes.stoppelmap.dto.data

import kotlinx.serialization.Serializable

@Serializable
data class StoppelMapData(
    val version: Long,
    val schemaVersion: Long,
    val note: String? = null,
    val seasonYear: Int,
    val definitions: Definitions,
    val map: Map,
    val transportation: Transportation,
)

@Serializable
data class Definitions(
    val tags: List<Tag>,
    val products: List<Product>,
    val services: List<Service>,
)
