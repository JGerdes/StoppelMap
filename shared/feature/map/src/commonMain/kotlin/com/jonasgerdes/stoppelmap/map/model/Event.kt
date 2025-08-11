package com.jonasgerdes.stoppelmap.map.model

import com.jonasgerdes.stoppelmap.data.shared.Localized
import com.jonasgerdes.stoppelmap.schedule.model.EventSlug
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

data class Event(
    val slug: EventSlug,
    val name: Localized<String>,
    val start: LocalDateTime,
    val end: LocalDateTime?,
    val description: Localized<String>?,
    val isBookmarked: Boolean,
)

data class EventDay(
    val date: LocalDate,
    val events: List<Event>
)