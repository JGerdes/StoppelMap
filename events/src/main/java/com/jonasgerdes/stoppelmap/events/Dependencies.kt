package com.jonasgerdes.stoppelmap.events

import com.jonasgerdes.stoppelmap.events.entity.Day
import com.jonasgerdes.stoppelmap.events.usecase.GetEventsByDayUseCase
import com.jonasgerdes.stoppelmap.events.usecase.GetStallBySlugUseCase
import com.jonasgerdes.stoppelmap.events.view.EventsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val eventsModule = module {

    single { GetEventsByDayUseCase(eventRepository = get(), globalInfoProvider = get()) }
    single { GetStallBySlugUseCase(stallRepository = get()) }

    viewModel { (day: Day) ->
        EventsViewModel(
            day = day,
            getEventsByDay = get(),
            getStallBySlug = get()
        )
    }
}