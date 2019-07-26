package com.jonasgerdes.stoppelmap.preperation.db

data class Field(
    val fieldPath: String,
    val columnName: String,
    val affinity: String,
    val notNull: Boolean
)