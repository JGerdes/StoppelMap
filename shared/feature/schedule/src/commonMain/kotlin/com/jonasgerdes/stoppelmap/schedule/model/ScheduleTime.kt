package com.jonasgerdes.stoppelmap.schedule.model

import kotlinx.datetime.LocalTime

data class ScheduleTime(
    val time: LocalTime,
    val events: List<Event>
)
