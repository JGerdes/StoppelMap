package com.jonasgerdes.stoppelmap.preperation.db

data class Database(
    val version: Int,
    val identityHash: String,
    val entities: List<Entity>,
    val views: List<Any>,
    val setupQueries: List<String>
)