package com.jonasgerdes.stoppelmap.usecase

import com.jonasgerdes.stoppelmap.base.contract.ClockProvider
import com.jonasgerdes.stoppelmap.base.contract.SeasonProvider
import kotlinx.datetime.toJavaLocalDateTime
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlin.time.toKotlinDuration

class IsCurrentYearsSeasonJustOverUseCase(
    private val seasonProvider: SeasonProvider,
    private val clockProvider: ClockProvider
) {

    operator fun invoke(): Boolean {
        val difference = java.time.Duration.between(
            seasonProvider.getThisYearsSeason().end.toJavaLocalDateTime(),
            clockProvider.nowAsLocalDateTime().toJavaLocalDateTime()
        ).toKotlinDuration()
        return difference.isPositive() && difference < 2.toDuration(DurationUnit.DAYS)
    }
}
