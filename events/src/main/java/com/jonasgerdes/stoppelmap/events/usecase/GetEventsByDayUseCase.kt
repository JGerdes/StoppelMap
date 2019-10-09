package com.jonasgerdes.stoppelmap.events.usecase

import com.jonasgerdes.stoppelmap.core.domain.GlobalInfoProvider
import com.jonasgerdes.stoppelmap.data.repository.EventRepository
import com.jonasgerdes.stoppelmap.events.entity.Day
import com.jonasgerdes.stoppelmap.model.events.Event
import org.threeten.bp.LocalDate
import org.threeten.bp.Month

class GetEventsByDayUseCase(
    private val eventRepository: EventRepository,
    private val globalInfoProvider: GlobalInfoProvider
) {

    suspend operator fun invoke(day: Day): List<Event> {
        return eventRepository.getEventsByDay(day.toLocalDate())
    }

    private fun Day.toLocalDate(): LocalDate {
        return globalInfoProvider.getCurrentSeason().days[id]
    }
}