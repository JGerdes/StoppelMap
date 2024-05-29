package com.jonasgerdes.stoppelmap.countdown.usecase

import co.touchlab.skie.configuration.annotations.DefaultArgumentInterop
import com.jonasgerdes.stoppelmap.base.contract.ClockProvider
import com.jonasgerdes.stoppelmap.base.contract.SeasonProvider
import com.jonasgerdes.stoppelmap.countdown.model.CountDown
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.plus
import kotlin.time.Duration.Companion.minutes

class GetOpeningCountDownUseCase
internal constructor(
    private val clockProvider: ClockProvider,
    private val seasonProvider: SeasonProvider,
) {
    @DefaultArgumentInterop.Enabled
    operator fun invoke(minuteOffset: Int = 0): CountDown {
        val currentOrNextSeason = seasonProvider.getCurrentOrNextSeason()

        val now = clockProvider.nowAsInstant().plus(minuteOffset.minutes)
        val opening = clockProvider.toInstant(currentOrNextSeason.start)
            // Since we don't show seconds, add 1 minute so visible time left adds up to target.
            // e.g. 18:10 -> 18:30 - rather show 20 minutes then 19 (with e.g. 21s not shown)
            .plus(1L, DateTimeUnit.MINUTE)

        return when {
            opening > now -> {
                (opening - now).toComponents { days, hours, minutes, seconds, _ ->
                    CountDown.InFuture(
                        daysLeft = days.toInt().coerceAtLeast(0),
                        hoursLeft = hours.coerceAtLeast(0),
                        minutesLeft = minutes.coerceAtLeast(0),
                        secondsLeft = seconds.coerceAtLeast(0),
                        season = currentOrNextSeason,
                    )
                }
            }

            else -> CountDown.OnGoing(currentOrNextSeason)
        }
    }
}