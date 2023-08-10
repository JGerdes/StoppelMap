package com.jonasgerdes.stoppelmap.schedule.usecase

import com.jonasgerdes.stoppelmap.base.contract.ClockProvider
import com.jonasgerdes.stoppelmap.schedule.repository.BookmarkedEventsRepository
import com.jonasgerdes.stoppelmap.schedule.repository.EventRepository
import kotlinx.coroutines.flow.map
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class GetNextBookmarkedEventUseCase(
    private val bookmarkedEventsRepository: BookmarkedEventsRepository,
    private val eventsRepository: EventRepository,
    private val clockProvider: ClockProvider,
) {

    operator fun invoke(maxEvents: Int = 3) = bookmarkedEventsRepository.getBookmarkedEvents()
        .map { bookmarked ->
            val now = clockProvider.nowAsInstant()
            // Simulating us being in past so we still see events 15 after they're started.
            val adjustedNow =
                clockProvider.toLocalDateTime(now.minus(15.toDuration(DurationUnit.MINUTES)))

            eventsRepository.getAllEvents()
                .filter { bookmarked.contains(it.slug) }
                .filter {
                    it.start > adjustedNow && clockProvider.toInstant(it.start) - now < 24.toDuration(
                        DurationUnit.HOURS
                    )
                }
                .sortedBy { it.start }
                .take(maxEvents)
        }

}
