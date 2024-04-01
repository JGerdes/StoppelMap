package com.jonasgerdes.stoppelmap.schedule.ui

import com.jonasgerdes.stoppelmap.base.contract.ClockProvider
import com.jonasgerdes.stoppelmap.schedule.repository.BookmarkedEventsRepository
import com.jonasgerdes.stoppelmap.schedule.repository.EventRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ScheduleDependencies : KoinComponent {
    val eventRepository: EventRepository by inject()
    val bookmarkedEventsRepository: BookmarkedEventsRepository by inject()
    val clockProvider: ClockProvider by inject()
}