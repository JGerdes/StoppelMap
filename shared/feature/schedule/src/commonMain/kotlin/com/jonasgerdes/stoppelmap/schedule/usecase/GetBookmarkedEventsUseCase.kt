package com.jonasgerdes.stoppelmap.schedule.usecase

import com.jonasgerdes.stoppelmap.base.contract.ClockProvider
import com.jonasgerdes.stoppelmap.schedule.model.BookmarkedEvents
import com.jonasgerdes.stoppelmap.schedule.model.Event
import com.jonasgerdes.stoppelmap.schedule.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetBookmarkedEventsUseCase(
    private val eventRepository: EventRepository,
    private val clockProvider: ClockProvider
) {
    operator fun invoke(): Flow<BookmarkedEvents> =
        eventRepository.getAllBookmarkedEvents().map { bookmarkedEvents ->
            if (bookmarkedEvents.isEmpty()) BookmarkedEvents.None
            else {
                val now = clockProvider.nowAsLocalDateTime().date
                BookmarkedEvents.Some(
                    past = bookmarkedEvents.filter { it.start.date < now }.sortedWith(eventComparator),
                    upcoming = bookmarkedEvents.filter { it.start.date >= now }.sortedWith(eventComparator)
                )
            }
        }

}

private val eventComparator = Comparator<Event> { first, second ->
    when {
        first.start != second.start -> first.start.compareTo(second.start)
        first.locationSlug == second.locationSlug -> 0
        first.locationSlug == null -> -1
        second.locationSlug == null -> 1
        else -> first.locationSlug.compareTo(second.locationSlug)
    }
}