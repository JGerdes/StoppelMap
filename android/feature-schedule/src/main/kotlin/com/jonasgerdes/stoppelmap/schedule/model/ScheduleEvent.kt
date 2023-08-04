package com.jonasgerdes.stoppelmap.schedule.model

import com.jonasgerdes.stoppelmap.data.Event

data class ScheduleEvent(
    val event: Event,
    val notificationActive: Boolean,
)
