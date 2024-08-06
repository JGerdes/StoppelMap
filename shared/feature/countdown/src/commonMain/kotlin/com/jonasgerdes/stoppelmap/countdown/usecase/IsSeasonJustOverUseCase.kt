package com.jonasgerdes.stoppelmap.countdown.usecase

import com.jonasgerdes.stoppelmap.base.contract.ClockProvider
import com.jonasgerdes.stoppelmap.base.contract.SeasonProvider
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil

internal class IsCurrentYearsSeasonJustOverUseCase(
    private val seasonProvider: SeasonProvider,
    private val clockProvider: ClockProvider,
    private val localTimeZone: TimeZone,
) {

    operator fun invoke(): Boolean = with(clockProvider) {
        val daysSinceEnd = seasonProvider.getThisYearsSeason().end.asInstant()
            .daysUntil(
                clockProvider.nowAsLocalDateTime().asInstant(),
                localTimeZone
            )
        return daysSinceEnd in 1..2
    }
}
