package com.jonasgerdes.stoppelmap.server.scheduler

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

class Task(
    val schedule: Schedule,
    val executeOnceImmediately: Boolean = false,
    private val doWork: suspend () -> Unit,
) {
    suspend operator fun invoke() = doWork()
}

sealed interface Schedule {
    data class Daily(val atHour: Int = 0, val atMinute: Int = 0) : Schedule
    data class Hourly(val atMinute: Int = 0) : Schedule
    data class RepeatEvery(val duration: Duration) : Schedule
}

//TODO: Replace with something like https://github.com/quartz-scheduler/quartz
class TaskScheduler(
    private val tasks: List<Task>,
    private val clockProvider: ClockProvider,
) {
    suspend fun run() {
        coroutineScope {
            tasks.forEach { task ->
                launch {
                    if (task.executeOnceImmediately) {
                        task()
                    }
                    while (true) {
                        task.schedule.delayToNextOccurrenceAfter(clockProvider.now())
                        task()
                    }
                }
            }
        }
    }
}

private val timeZone = TimeZone.UTC
private suspend fun Schedule.delayToNextOccurrenceAfter(now: Instant) {
    when (this) {
        is Schedule.Daily -> {
            val nowTomorrow = (now + 1.days).toLocalDateTime(timeZone)
            val nextOccurrence = LocalDateTime(
                year = nowTomorrow.year,
                month = nowTomorrow.month,
                dayOfMonth = nowTomorrow.dayOfMonth,
                hour = atHour,
                minute = atMinute,
                second = 0,
                nanosecond = 0,
            ).toInstant(timeZone)
            delay(nextOccurrence - now)
        }

        is Schedule.Hourly -> {
            val nowInOneHour = (now + 1.hours).toLocalDateTime(timeZone)
            val nextOccurrence = LocalDateTime(
                year = nowInOneHour.year,
                month = nowInOneHour.month,
                dayOfMonth = nowInOneHour.dayOfMonth,
                hour = nowInOneHour.hour,
                minute = atMinute,
                second = 0,
                nanosecond = 0,
            ).toInstant(timeZone)
            delay(nextOccurrence - now)
        }

        is Schedule.RepeatEvery -> delay(duration)
    }
}
