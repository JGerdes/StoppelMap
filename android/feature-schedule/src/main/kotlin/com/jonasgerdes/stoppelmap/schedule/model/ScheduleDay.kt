package com.jonasgerdes.stoppelmap.schedule.model

import kotlinx.datetime.LocalDate

data class ScheduleDay(
    val date: LocalDate,
    val timeSlots: List<ScheduleTime>
)
