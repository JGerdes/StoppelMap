package com.jonasgerdes.stoppelmap.dto.data

import kotlinx.serialization.Serializable

@Serializable
data class StoppelMapData(
    val version: Int,
    val schemaVersion: Int,
    val note: String? = null,
    val seasonYear: Int,
    val definitions: Definitions,
    val map: Map,
    val schedule: Schedule,
    val transportation: Transportation,
)

@Serializable
data class Definitions(
    val tags: List<Tag>,
    val subTypes: List<SubType>,
    val products: List<Product>,
    val services: List<Service>,
    val persons: List<Person>,
    val operators: List<Operator>,
)
