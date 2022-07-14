package com.jonasgerdes.stoppelmap.home.usecase

import kotlinx.datetime.*
import java.time.Month


class GetTimeLeftToOpeningUseCase {

    // TODO: Get rid of duplicated code

    private val stoppelmarktOpening = LocalDateTime(
        year = 2022,
        month = Month.AUGUST,
        dayOfMonth = 11,
        hour = 18,
        minute = 30,
        second = 0,
        nanosecond = 0
    )

    private val stoppelmarktTimeZone = TimeZone.of("Europe/Berlin")

    operator fun invoke(): Result {
        val now = Clock.System.now()
        val target = stoppelmarktOpening.toInstant(stoppelmarktTimeZone)
            // Since we don't show seconds, add 1 minute so visible time left adds up to target.
            // e.g. 18:10 -> 18:30 - rather show 20 minutes then 19 (with e.g. 21s not shown)
            .plus(1L, DateTimeUnit.MINUTE)

        return if (target > now) {
            (target - now).toComponents { days, hours, _, _, _ ->
                Result(
                    daysLeft = days.toInt().coerceAtLeast(0),
                    hoursLeft = hours.coerceAtLeast(0),
                )
            }
        } else {
            Result(daysLeft = 0, hoursLeft = 0)
        }
    }

    data class Result(
        val daysLeft: Int,
        val hoursLeft: Int,
    ) {
        val isOver: Boolean = daysLeft == 0 && hoursLeft == 0
    }
}
