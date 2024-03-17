package com.jonasgerdes.stoppelmap.countdown.usecase

import com.jonasgerdes.stoppelmap.base.contract.ClockProvider
import com.jonasgerdes.stoppelmap.base.contract.SeasonProvider
import com.jonasgerdes.stoppelmap.provider.stoppelmarktTimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toInstant

internal class IsCurrentYearsSeasonJustOverUseCase(
    private val seasonProvider: SeasonProvider,
    private val clockProvider: ClockProvider
) {

    operator fun invoke(): Boolean {
        val daysSinceEnd = seasonProvider.getThisYearsSeason().end.toInstant(stoppelmarktTimeZone)
            .daysUntil(
                clockProvider.nowAsLocalDateTime().toInstant(stoppelmarktTimeZone),
                stoppelmarktTimeZone
            )
        return daysSinceEnd in 1..2
    }
}
