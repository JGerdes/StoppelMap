package com.jonasgerdes.stoppelmap.events.entity

import com.jonasgerdes.stoppelmap.model.map.Stall
import org.threeten.bp.OffsetDateTime

data class Event(
    val title: String,
    val location: Stall?,
    val start: OffsetDateTime,
    val description: String?,
    val isFinished: Boolean
)