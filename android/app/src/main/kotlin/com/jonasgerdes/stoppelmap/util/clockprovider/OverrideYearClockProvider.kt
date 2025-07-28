package com.jonasgerdes.stoppelmap.util.clockprovider

import com.jonasgerdes.stoppelmap.base.contract.ClockProvider
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime

class OverrideYearClockProvider(
    private val realClockProvider: ClockProvider,
    private val year: Int,
) : ClockProvider {
    override fun nowAsInstant(): Instant = toInstant(nowAsLocalDateTime())

    override fun nowAsLocalDateTime(): LocalDateTime = realClockProvider.nowAsLocalDateTime().let {
        LocalDateTime(
            year = year,
            monthNumber = it.monthNumber,
            dayOfMonth = it.dayOfMonth,
            hour = it.hour,
            minute = it.minute,
            second = it.second,
            nanosecond = it.nanosecond
        )
    }

    override fun toLocalDateTime(instant: Instant): LocalDateTime = realClockProvider.toLocalDateTime(instant)

    override fun toInstant(localDateTime: LocalDateTime): Instant = realClockProvider.toInstant(localDateTime)

}
