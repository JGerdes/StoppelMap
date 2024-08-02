package com.jonasgerdes.stoppelmap.server.scheduler

import com.jonasgerdes.stoppelmap.server.monitoring.Monitoring
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.slf4j.Logger
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.seconds

interface Task {
    val name: String
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

    data class HoursOfDay(
        val hours: Set<Int>,
        override val executeOnceImmediately: Boolean = false
    ) : Schedule {
        constructor(vararg hours: Int, executeOnceImmediately: Boolean = false) :
                this(
                    hours = hours.toSet(),
                    executeOnceImmediately = executeOnceImmediately
                )
    }

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
    private val monitoring: Monitoring,
) {
    suspend fun run() {
        logger.info("TaskScheduler - Starting with ${tasks.size} tasks:")
        tasks.forEach {
            logger.info("                ${it::class.simpleName} (runs ${it.schedule})")
        }
        coroutineScope {
            // Wait 5 seconds for everything to settle
            delay(5.seconds)
            tasks.forEach { task ->
                launch {
                    if (task.schedule.executeOnceImmediately) {
                        runTask(task)
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
                            runTask(task)
                        }
                    }
                }
            }
        }
    }

    private suspend fun runTask(task: Task) {
        logger.info("Running task ${task::class.simpleName}")
        monitoring.recordTask(task.name) {
            task()
        }
    }
}

private val timeZone = TimeZone.UTC
private fun Schedule.durationToNextOccurrenceAfter(now: Instant): Duration? =
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
        is Schedule.HoursOfDay -> {
            val localNow = now.toLocalDateTime(timeZone)
            val hours = hours.sorted()
            val nextHour = hours.firstOrNull { it > localNow.hour }

            val nextOccurrence =
                if (nextHour != null) {
                    localNow.date.atTime(hour = nextHour, minute = 0)
                } else {
                    val localTomorrow = (now + 1.days).toLocalDateTime(timeZone).date
                    localTomorrow.atTime(hour = hours.first(), minute = 0)
                }.toInstant(timeZone)

            nextOccurrence - now
        }
    }
