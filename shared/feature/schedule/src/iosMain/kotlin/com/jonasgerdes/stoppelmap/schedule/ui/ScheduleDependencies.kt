package com.jonasgerdes.stoppelmap.schedule.ui

import com.jonasgerdes.stoppelmap.base.contract.ClockProvider
import com.jonasgerdes.stoppelmap.schedule.repository.EventRepository
import com.jonasgerdes.stoppelmap.schedule.usecase.GetScheduleDaysUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ScheduleDependencies : KoinComponent {
    val getScheduleDays: GetScheduleDaysUseCase by inject()
    val eventRepository: EventRepository by inject()
    val clockProvider: ClockProvider by inject()
}