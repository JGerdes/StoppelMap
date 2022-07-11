package com.jonasgerdes.stoppelmap.home.usecase

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.*
import java.time.Month

private const val UPDATE_INTERVAL_MS = 1_000L

class GetOpeningCountDownUseCase {

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

    operator fun invoke() = flow {
        var result: Result
        do {
            result = calculateTimeLeft()
            emit(result)
            delay(UPDATE_INTERVAL_MS)
        } while (!result.isOver)
    }

    private fun calculateTimeLeft(): Result {
        val now = Clock.System.now()
        val target = stoppelmarktOpening.toInstant(stoppelmarktTimeZone)
            // Since we don't show seconds, add 1 minute so visible time left adds up to target.
            // e.g. 18:10 -> 18:30 - rather show 20 minutes then 19 (with e.g. 21s not shown)
            .plus(1L, DateTimeUnit.MINUTE)

        return if (target > now) {
            (target - now).toComponents { days, hours, minutes, _, _ ->
                Result(
                    daysLeft = days.toInt().coerceAtLeast(0),
                    hoursLeft = hours.coerceAtLeast(0),
                    minutesLeft = minutes.coerceAtLeast(0)
                )
            }
        } else {
            Result(daysLeft = 0, hoursLeft = 0, minutesLeft = 0)
        }
    }

    data class Result(
        val daysLeft: Int,
        val hoursLeft: Int,
        val minutesLeft: Int,
    ) {
        val isOver: Boolean = daysLeft == 0 && hoursLeft == 0 && minutesLeft == 0
    }
}
