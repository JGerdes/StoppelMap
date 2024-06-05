package com.jonasgerdes.stoppelmap.server.data

import app.cash.sqldelight.ColumnAdapter
import kotlinx.datetime.Instant
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents

private val format = DateTimeComponents.Formats.ISO_DATE_TIME_OFFSET

internal val instantAdapter = object : ColumnAdapter<Instant, String> {
    override fun decode(databaseValue: String) = Instant.parse(databaseValue, format)

    override fun encode(value: Instant) = value.format(format)
}
