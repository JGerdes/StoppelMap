package com.jonasgerdes.stoppelmap.dto.data

import kotlinx.serialization.Serializable

// NOTE: For changes that a not backwards compatible, increase schemaVersion
@Serializable
data class StoppelMapData(
    val version: Int,
    val note: String? = null,
    val seasonYear: Int,
    val definitions: Definitions,
    val map: Map,
    val schedule: Schedule,
    val transportation: Transportation,
) {
    val schemaVersion: Int = StoppelMapData.schemaVersion

    companion object {
        const val schemaVersion: Int = 1
    }
}

@Serializable
data class Definitions(
    val tags: List<Tag>,
    val subTypes: List<SubType>,
    val products: List<Product>,
    val services: List<Service>,
    val persons: List<Person>,
    val operators: List<Operator>,
)
