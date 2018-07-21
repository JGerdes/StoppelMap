package com.jonasgerdes.stoppelmap.util

import org.threeten.bp.Instant
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

fun Date.toOffsetDateTime() = OffsetDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.of("+2"))

fun Int.squared() = this * this
