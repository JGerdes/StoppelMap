package com.jonasgerdes.stoppelmap.data.repository

import com.jonasgerdes.stoppelmap.data.StoppelmapDatabase
import org.threeten.bp.LocalDate
import javax.inject.Inject

class EventRepository @Inject constructor(
    private val database: StoppelmapDatabase
) {


    suspend fun getEventsByDay(localDate: LocalDate) = database.eventDao().getAllEventsAt(localDate)
}