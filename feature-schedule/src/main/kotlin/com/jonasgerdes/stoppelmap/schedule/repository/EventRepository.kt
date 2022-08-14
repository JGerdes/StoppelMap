package com.jonasgerdes.stoppelmap.schedule.repository

import androidx.core.text.HtmlCompat
import com.jonasgerdes.stoppelmap.data.Event
import com.jonasgerdes.stoppelmap.data.EventQueries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EventRepository(
    private val eventQueries: EventQueries
) {
    suspend fun getAllEvents() = withContext(Dispatchers.IO) {
        eventQueries.getAll().executeAsList().fixDescription()

    }

    suspend fun getAllOfficialEvents() = withContext(Dispatchers.IO) {
        eventQueries.getAllOfficialEvents().executeAsList().fixDescription()
    }
}

private fun List<Event>.fixDescription() = map {
    it.copy(
        description = it.description?.removeHtml()
    )
}

private fun String.removeHtml() =
    HtmlCompat.fromHtml(
        this,
        HtmlCompat.FROM_HTML_MODE_LEGACY
    ).toString().trim().ifBlank { null }
