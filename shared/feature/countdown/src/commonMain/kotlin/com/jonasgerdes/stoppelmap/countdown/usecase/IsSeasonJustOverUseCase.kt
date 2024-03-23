package com.jonasgerdes.stoppelmap.countdown.usecase

import com.jonasgerdes.stoppelmap.base.contract.ClockProvider
import com.jonasgerdes.stoppelmap.base.contract.SeasonProvider
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toInstant

internal class IsCurrentYearsSeasonJustOverUseCase(
    private val seasonProvider: SeasonProvider,
    private val clockProvider: ClockProvider,
    private val localTimeZone: TimeZone,
) {

    operator fun invoke(): Boolean {
        val daysSinceEnd = seasonProvider.getThisYearsSeason().end.toInstant(localTimeZone)
            .daysUntil(
                clockProvider.nowAsLocalDateTime().toInstant(localTimeZone),
                localTimeZone
            )
        return daysSinceEnd in 1..2
    }
}
