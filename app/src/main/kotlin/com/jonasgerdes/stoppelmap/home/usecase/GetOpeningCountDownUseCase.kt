package com.jonasgerdes.stoppelmap.home.usecase

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
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
