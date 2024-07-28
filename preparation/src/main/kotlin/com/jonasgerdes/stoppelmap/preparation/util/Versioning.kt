package com.jonasgerdes.stoppelmap.preparation.util

import kotlinx.datetime.Clock
import kotlinx.datetime.FixedOffsetTimeZone
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.toLocalDateTime

fun getVersion(commitSha: String): Version {
    val now = Clock.System.now().toLocalDateTime(FixedOffsetTimeZone(UtcOffset(hours = 1)))
    val beats = ((3600 * now.hour + 60 * now.minute) / 86.4f).toInt()
    val year = now.year - 2000
    val month = now.monthNumber
    val monthPadded = month.toString().padStart(2, '0')
    val day = now.dayOfMonth
    val dayPadded = day.toString().padStart(2, '0')
    val paddedBeats = beats.toString().padStart(3, '0')
    return Version(
        name = "v$year.$monthPadded.$dayPadded.$paddedBeats-$commitSha",
        code = 10000000 * year + 100000 * month + 1000 * day + beats
    )
}

data class Version(val name: String, val code: Int)
