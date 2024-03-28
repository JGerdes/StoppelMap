package com.jonasgerdes.stoppelmap.data

import app.cash.sqldelight.ColumnAdapter
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.format.DateTimeFormatter

private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

internal val localDateTimeAdapter = object : ColumnAdapter<LocalDateTime, String> {
    override fun decode(databaseValue: String) =
        java.time.LocalDateTime.parse(databaseValue, formatter).toKotlinLocalDateTime()

    override fun encode(value: LocalDateTime) = value.toJavaLocalDateTime().format(formatter)
}
