package com.jonasgerdes.stoppelmap.schedule.model

import com.jonasgerdes.stoppelmap.data.shared.Localized
import kotlinx.datetime.LocalDateTime

typealias EventSlug = String

data class Event(
    val slug: EventSlug,
    val name: String,
    val start: LocalDateTime,
    val end: LocalDateTime?,
    val locationSlug: String?,
    val locationName: String?,
    val description: Localized<String>?,
    val isBookmarked: Boolean,
)
