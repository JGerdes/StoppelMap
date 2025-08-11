package com.jonasgerdes.stoppelmap.schedule.model

import com.jonasgerdes.stoppelmap.data.shared.Localized
import kotlinx.datetime.LocalDateTime

data class LocationEvent(
    val slug: EventSlug,
    val name: Localized<String>,
    val start: LocalDateTime,
    val end: LocalDateTime?,
    val description: Localized<String>?,
    val isBookmarked: Boolean,
)
