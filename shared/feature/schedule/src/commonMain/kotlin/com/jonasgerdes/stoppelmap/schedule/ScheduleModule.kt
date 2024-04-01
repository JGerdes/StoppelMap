package com.jonasgerdes.stoppelmap.schedule

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.jonasgerdes.stoppelmap.base.contract.PreferencesPathFactory
import com.jonasgerdes.stoppelmap.data.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.schedule.repository.BookmarkedEventsRepository
import com.jonasgerdes.stoppelmap.schedule.repository.EventRepository
import com.jonasgerdes.stoppelmap.schedule.usecase.GetNextBookmarkedEventUseCase
import com.jonasgerdes.stoppelmap.schedule.usecase.GetNextOfficialEventUseCase
import okio.Path.Companion.toPath
import org.koin.dsl.module

val scheduleModule = module {

    single {
        EventRepository(eventQueries = get<StoppelMapDatabase>().eventQueries)
    }

    single {
        BookmarkedEventsRepository(
            dataStore = PreferenceDataStoreFactory.createWithPath(
                corruptionHandler = null,
                migrations = emptyList(),
                produceFile = { get<PreferencesPathFactory>().create("scheduleUserData").toPath() },
            )
        )
    }

    factory {
        GetNextOfficialEventUseCase(
            eventRepository = get(),
            clockProvider = get()
        )
    }

    factory {
        GetNextBookmarkedEventUseCase(
            bookmarkedEventsRepository = get(),
            eventsRepository = get(),
            clockProvider = get()
        )
    }
}
