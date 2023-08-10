package com.jonasgerdes.stoppelmap.schedule

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.jonasgerdes.stoppelmap.data.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.schedule.repository.BookmarkedEventsRepository
import com.jonasgerdes.stoppelmap.schedule.repository.EventRepository
import com.jonasgerdes.stoppelmap.schedule.ui.ScheduleViewModel
import com.jonasgerdes.stoppelmap.schedule.usecase.GetNextBookmarkedEventUseCase
import com.jonasgerdes.stoppelmap.schedule.usecase.GetNextOfficialEventUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "scheduleUserData")

val scheduleModule = module {

    single {
        EventRepository(eventQueries = get<StoppelMapDatabase>().eventQueries)
    }

    single {
        BookmarkedEventsRepository(dataStore = get<Context>().dataStore)
    }

    factory {
        GetNextOfficialEventUseCase(
            eventRepository = get(),
            clockProvider = get()
        )
    }

    viewModel {
        ScheduleViewModel(
            eventRepository = get(),
            bookmarkedEventsRepository = get(),
            clockProvider = get()
        )
    }
}
