package com.jonasgerdes.stoppelmap.model.common

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "images")
data class Image(
    @PrimaryKey
    val file: String,
    val reference: String,
    val type: String,
    val author: String?,
    val license: String?
)