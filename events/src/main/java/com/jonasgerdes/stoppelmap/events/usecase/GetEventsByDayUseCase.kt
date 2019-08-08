package com.jonasgerdes.stoppelmap.events.usecase

import com.jonasgerdes.stoppelmap.data.repository.EventRepository
import com.jonasgerdes.stoppelmap.events.entity.Day
import com.jonasgerdes.stoppelmap.model.events.Event
import org.threeten.bp.LocalDate
import org.threeten.bp.Month

class GetAllEventsUseCase(
    private val eventRepository: EventRepository
) {

    suspend operator fun invoke(): Map<Day, List<Event>> {
        val events = eventRepository.getAllEvents()
        return events.groupBy { it.start.toLocalDate().toDay() }
    }

    private fun LocalDate.toDay(): Day {
        //TODO: make this less hardcoded
        return when {
            month != Month.AUGUST -> Day(-1)
            dayOfMonth >= 15 && dayOfMonth <= 20 -> Day(dayOfMonth - 15)
            else -> Day(-1)
        }
    }
}