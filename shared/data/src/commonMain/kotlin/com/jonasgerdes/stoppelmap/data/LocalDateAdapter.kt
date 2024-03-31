package com.jonasgerdes.stoppelmap.data

import app.cash.sqldelight.ColumnAdapter
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.char

private val format = LocalDate.Format {
    year()
    char('-')
    monthNumber()
    char('-')
    dayOfMonth()
}

internal val localDateAdapter = object : ColumnAdapter<LocalDate, String> {
    override fun decode(databaseValue: String) = format.parse(databaseValue)

    override fun encode(value: LocalDate) = format.format(value)
}
