package com.jonasgerdes.stoppelmap.schedule

import com.jonasgerdes.stoppelmap.data.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.schedule.repository.EventRepository
import com.jonasgerdes.stoppelmap.schedule.ui.ScheduleViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val scheduleModule = module {

    single {
        EventRepository(eventQueries = get<StoppelMapDatabase>().eventQueries)
    }

    factory {
        GetNextOfficialEventUseCase(
            eventRepository = get(),
            getCurrentLocalDateTime = get()
        )
    }

    viewModel {
        ScheduleViewModel(
            eventRepository = get(),
            getCurrentLocalDateTime = get()
        )
    }
}
