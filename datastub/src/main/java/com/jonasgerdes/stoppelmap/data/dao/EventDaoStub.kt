package com.jonasgerdes.stoppelmap.data.dao

import com.jonasgerdes.stoppelmap.model.events.Event
import org.threeten.bp.format.DateTimeFormatter

private val LOCAL_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd")

class EventDaoStub(private val events: MutableList<Event>) : EventDao() {
    override suspend fun getAllEvents(): List<Event> =
        events.sortedWith(compareBy({ it.start }, { it.name }))

    override suspend fun getAllEventsAt(date: String): List<Event> = events.filter {
        it.start.toLocalDate().format(LOCAL_DATE_FORMAT) == date
    }.sortedWith(compareBy({ it.start }, { it.name }))
}