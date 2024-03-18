package com.jonasgerdes.stoppelmap.provider

import com.jonasgerdes.stoppelmap.base.contract.ClockProvider
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class StoppelmarktClockProvider(
    private val localTimeZone: TimeZone,
) : ClockProvider {

    override fun nowAsInstant() = Clock.System.now()

    override fun nowAsLocalDateTime() = nowAsInstant().toLocalDateTime(localTimeZone)

    override fun toLocalDateTime(instant: Instant) = instant.toLocalDateTime(localTimeZone)

    override fun toInstant(localDateTime: LocalDateTime) = localDateTime.toInstant(
        localTimeZone
    )
}
