package com.jonasgerdes.stoppelmap.schedule.model

import com.jonasgerdes.stoppelmap.data.Event
import kotlinx.datetime.LocalDate

data class ScheduleDay(
    val date: LocalDate,
    val events: List<Event>
)
