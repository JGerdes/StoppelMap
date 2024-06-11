package com.jonasgerdes.stoppelmap.server.scheduler

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.slf4j.Logger
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.seconds

interface Task {
    val schedule: Schedule
    suspend operator fun invoke()
}

sealed interface Schedule {
    val executeOnceImmediately: Boolean

    data object Once : Schedule {
        override val executeOnceImmediately = true
    }

    data class Daily(
        val atHour: Int = 0,
        val atMinute: Int = 0,
        override val executeOnceImmediately: Boolean = false
    ) : Schedule

    data class Hourly(
        val atMinute: Int = 0,
        override val executeOnceImmediately: Boolean = false
    ) : Schedule

    data class RepeatEvery(
        val duration: Duration,
        override val executeOnceImmediately: Boolean = false
    ) : Schedule
}

//TODO: Replace with something like https://github.com/quartz-scheduler/quartz
class TaskScheduler(
    private val tasks: List<Task>,
    private val clockProvider: ClockProvider,
    private val logger: Logger,
) {
    suspend fun run() {
        coroutineScope {
            // Wait 5 seconds for everything to settle
            delay(5.seconds)
            tasks.forEach { task ->
                launch {
                    if (task.schedule.executeOnceImmediately) {
                        logger.info("Running task ${task::class.simpleName}")
                        task()
                    }
                    var done = false
                    while (!done) {
                        val delayDuration =
                            task.schedule.durationToNextOccurrenceAfter(clockProvider.now())

                        if (delayDuration == null) {
                            done = true
                        } else {
                            logger.trace("Waiting $delayDuration for next occurrence of task ${task::class.simpleName}")
                            delay(delayDuration)
                            logger.info("Running task ${task::class.simpleName}")
                            task()
                        }
                    }
                }
            }
        }
    }
}

private val timeZone = TimeZone.UTC
private suspend fun Schedule.durationToNextOccurrenceAfter(now: Instant): Duration? =
    when (this) {
        is Schedule.Once -> null
        is Schedule.Daily -> {
            val localNow = now.toLocalDateTime(timeZone)
            val occurrenceToday = LocalDateTime(
                year = localNow.year,
                month = localNow.month,
                dayOfMonth = localNow.dayOfMonth,
                hour = atHour,
                minute = atMinute,
                second = 0,
                nanosecond = 0,
            ).toInstant(timeZone)
            val nextOccurrence =
                if (occurrenceToday > now) occurrenceToday
                else (occurrenceToday + 1.days)
            nextOccurrence - now
        }

        is Schedule.Hourly -> {
            val localNow = now.toLocalDateTime(timeZone)
            val occurrenceThisHour = LocalDateTime(
                year = localNow.year,
                month = localNow.month,
                dayOfMonth = localNow.dayOfMonth,
                hour = localNow.hour,
                minute = atMinute,
                second = 0,
                nanosecond = 0,
            ).toInstant(timeZone)
            val nextOccurrence =
                if (occurrenceThisHour > now) occurrenceThisHour
                else (occurrenceThisHour + 1.hours)
            nextOccurrence - now
        }

        is Schedule.RepeatEvery -> duration
    }
