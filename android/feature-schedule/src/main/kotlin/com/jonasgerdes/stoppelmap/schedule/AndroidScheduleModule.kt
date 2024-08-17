package com.jonasgerdes.stoppelmap.schedule

import com.jonasgerdes.stoppelmap.schedule.ui.ScheduleViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val androidScheduleModule = module {

    viewModel {
        ScheduleViewModel(
            getScheduleDays = get(),
            eventRepository = get(),
            clockProvider = get(),
            getBookmarkedEvents = get(),
        )
    }
}
