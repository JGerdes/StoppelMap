package com.jonasgerdes.stoppelmap.countdown.usecase

import com.jonasgerdes.stoppelmap.base.contract.ClockProvider
import com.jonasgerdes.stoppelmap.countdown.model.CountDown
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.plus
import java.time.Month


internal class GetOpeningCountDownUseCase(
    private val clockProvider: ClockProvider
) {

    // TODO: Calculate dates dynamically

    private val stoppelmarktOpening = LocalDateTime(
        year = 2023,
        month = Month.AUGUST,
        dayOfMonth = 10,
        hour = 18,
        minute = 30,
        second = 0,
        nanosecond = 0
    )

    private val stoppelmarktClosing = LocalDateTime(
        year = 2022,
        month = Month.AUGUST,
        dayOfMonth = 16,
        hour = 22,
        minute = 30,
        second = 0,
        nanosecond = 0
    )

    operator fun invoke(): CountDown {
        val now = clockProvider.nowAsInstant()
        val opening = clockProvider.toInstant(stoppelmarktOpening)
            // Since we don't show seconds, add 1 minute so visible time left adds up to target.
            // e.g. 18:10 -> 18:30 - rather show 20 minutes then 19 (with e.g. 21s not shown)
            .plus(1L, DateTimeUnit.MINUTE)

        val closing = clockProvider.toInstant(stoppelmarktClosing)

        return when {
            now < closing -> CountDown.OnGoing
            opening > now -> {
                (opening - now).toComponents { days, hours, minutes, _, _ ->
                    CountDown.InFuture(
                        daysLeft = days.toInt().coerceAtLeast(0),
                        hoursLeft = hours.coerceAtLeast(0),
                        minutesLeft = minutes.coerceAtLeast(0),
                        year = stoppelmarktOpening.year
                    )
                }
            }
            else -> CountDown.OnGoing
        }
    }
}
