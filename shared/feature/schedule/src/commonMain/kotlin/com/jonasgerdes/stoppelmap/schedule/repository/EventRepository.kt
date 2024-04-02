package com.jonasgerdes.stoppelmap.schedule.repository

import com.jonasgerdes.stoppelmap.data.EventQueries
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class EventRepository(
    private val eventQueries: EventQueries
) {
    suspend fun getAllEvents() = withContext(Dispatchers.IO) {
        eventQueries.getAll().executeAsList()
    }

    suspend fun getAllOfficialEvents() = withContext(Dispatchers.IO) {
        try {
            eventQueries.getAllOfficialEvents().executeAsList()
        } catch (cancellationException: CancellationException) {
            throw cancellationException
        } catch (exception: Exception) {
            emptyList()
        }
    }
}
