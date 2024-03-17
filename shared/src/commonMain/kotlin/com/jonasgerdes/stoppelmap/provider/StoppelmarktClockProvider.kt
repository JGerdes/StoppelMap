package com.jonasgerdes.stoppelmap.provider

import com.jonasgerdes.stoppelmap.base.contract.ClockProvider
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class StoppelmarktClockProvider : ClockProvider {

    override fun nowAsInstant() = Clock.System.now()

    override fun nowAsLocalDateTime() = nowAsInstant().toLocalDateTime(stoppelmarktTimeZone)

    override fun toLocalDateTime(instant: Instant) = instant.toLocalDateTime(stoppelmarktTimeZone)

    override fun toInstant(localDateTime: LocalDateTime) = localDateTime.toInstant(
        stoppelmarktTimeZone
    )
}
