package com.jonasgerdes.stoppelmap.preperation.db

data class PrimaryKey(
    val columnNames: List<String>,
    val autoGenerate: Boolean
)