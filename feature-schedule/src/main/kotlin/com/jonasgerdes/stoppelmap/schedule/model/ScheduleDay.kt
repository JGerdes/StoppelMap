package com.jonasgerdes.stoppelmap.schedule.model

import com.jonasgerdes.stoppelmap.data.Event
import kotlinx.datetime.DayOfWeek

data class ScheduleDay(
    val dayOfWeek: DayOfWeek,
    val events: List<Event>
)
