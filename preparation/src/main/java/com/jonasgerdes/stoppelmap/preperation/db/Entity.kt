package com.jonasgerdes.stoppelmap.preperation.db

data class Entity(
    val tableName: String,
    val createSql: String,
    val fields: List<Field>,
    val primaryKey: PrimaryKey,
    val indices: List<Any>,
    val foreignKeys: List<Any>
)