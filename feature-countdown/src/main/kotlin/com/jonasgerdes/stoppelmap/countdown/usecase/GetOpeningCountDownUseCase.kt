package com.jonasgerdes.stoppelmap.countdown.usecase

import com.jonasgerdes.stoppelmap.countdown.model.CountDown
import kotlinx.datetime.*
import java.time.Month


internal class GetOpeningCountDownUseCase {

    // TODO: Calculate dates dynamically

    private val stoppelmarktOpening = LocalDateTime(
        year = 2022,
        month = Month.AUGUST,
        dayOfMonth = 11,
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
        minute = 0,
        second = 0,
        nanosecond = 0
    )

    private val stoppelmarktTimeZone = TimeZone.of("Europe/Berlin")

    operator fun invoke(): CountDown {
        val now = Clock.System.now()
        val opening = stoppelmarktOpening.toInstant(stoppelmarktTimeZone)
            // Since we don't show seconds, add 1 minute so visible time left adds up to target.
            // e.g. 18:10 -> 18:30 - rather show 20 minutes then 19 (with e.g. 21s not shown)
            .plus(1L, DateTimeUnit.MINUTE)

        val closing = stoppelmarktClosing.toInstant(stoppelmarktTimeZone)

        return when {
            opening > now -> {
                (opening - now).toComponents { days, hours, minutes, _, _ ->
                    CountDown.InFuture(
                        daysLeft = days.toInt().coerceAtLeast(0),
                        hoursLeft = hours.coerceAtLeast(0),
                        minutesLeft = minutes.coerceAtLeast(0)
                    )
                }
            }
            now > closing -> CountDown.InPast
            else -> CountDown.OnGoing
        }
    }
}
