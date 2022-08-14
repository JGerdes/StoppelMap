package com.jonasgerdes.stoppelmap.util

import com.jonasgerdes.stoppelmap.base.contract.ClockProvider
import kotlinx.datetime.*

private val stoppelmarktTimeZone = TimeZone.of("Europe/Berlin")

class StoppelmarktClockProvider : ClockProvider {

    private val mockDateTime: LocalDateTime? =
        // null
        LocalDateTime(2022, Month.AUGUST, 16, 23, 0)

    override fun nowAsInstant() =
        mockDateTime?.toInstant(stoppelmarktTimeZone) ?: Clock.System.now()

    override fun nowAsLocalDateTime() = nowAsInstant().toLocalDateTime(stoppelmarktTimeZone)

    override fun toLocalDateTime(instant: Instant) = instant.toLocalDateTime(stoppelmarktTimeZone)

    override fun toInstant(localDateTime: LocalDateTime) = localDateTime.toInstant(
        stoppelmarktTimeZone
    )
}
