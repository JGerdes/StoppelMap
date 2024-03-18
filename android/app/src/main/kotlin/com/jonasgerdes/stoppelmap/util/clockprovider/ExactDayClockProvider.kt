package com.jonasgerdes.stoppelmap.util.clockprovider

import com.jonasgerdes.stoppelmap.base.contract.ClockProvider
import com.jonasgerdes.stoppelmap.base.contract.SeasonProvider
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.minus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class ExactDayClockProvider(
    private val seasonProvider: SeasonProvider,
    private val realClockProvider: ClockProvider,
    private val localTimeZone: TimeZone,
    private val dayOfWeek: DayOfWeek,
) : ClockProvider {
    override fun nowAsInstant(): Instant = toInstant(nowAsLocalDateTime())

    override fun nowAsLocalDateTime(): LocalDateTime {
        val now = realClockProvider.nowAsLocalDateTime()
        val currentSeason = seasonProvider.getCurrentOrNextSeason()

        val day = currentSeason.days.firstOrNull {
            it.dayOfWeek == dayOfWeek
        } ?: currentSeason.start.date.minus(1, DateTimeUnit.DAY)

        return day.atTime(now.time)
    }

    override fun toLocalDateTime(instant: Instant): LocalDateTime =
        instant.toLocalDateTime(localTimeZone)

    override fun toInstant(localDateTime: LocalDateTime): Instant =
        localDateTime.toInstant(localTimeZone)

}
