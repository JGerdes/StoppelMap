package com.jonasgerdes.stoppelmap.data

import com.squareup.sqldelight.ColumnAdapter
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate
import java.time.format.DateTimeFormatter

private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

internal val localDateAdapter = object : ColumnAdapter<LocalDate, String> {
    override fun decode(databaseValue: String) =
        java.time.LocalDate.parse(databaseValue, formatter).toKotlinLocalDate()

    override fun encode(value: LocalDate) = value.toJavaLocalDate().format(formatter)
}
