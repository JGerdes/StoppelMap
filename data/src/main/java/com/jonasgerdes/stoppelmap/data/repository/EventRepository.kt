package com.jonasgerdes.stoppelmap.data.repository

import com.jonasgerdes.stoppelmap.data.StoppelmapDatabase
import org.threeten.bp.LocalDate

class EventRepository(
    private val database: StoppelmapDatabase
) {


    suspend fun getEventsByDay(localDate: LocalDate) = database.eventDao().getAllEventsAt(localDate)
}