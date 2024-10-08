package com.jonasgerdes.stoppelmap.schedule

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.jonasgerdes.stoppelmap.base.contract.PreferencesPathFactory
import com.jonasgerdes.stoppelmap.data.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.schedule.repository.EventRepository
import com.jonasgerdes.stoppelmap.schedule.usecase.GetBookmarkedEventsUseCase
import com.jonasgerdes.stoppelmap.schedule.usecase.GetScheduleDaysUseCase
import okio.Path.Companion.toPath
import org.koin.dsl.module

val scheduleModule = module {

    single {
        EventRepository(
            eventQueries = get<StoppelMapDatabase>().eventQueries,
            localizedStringQueries = get<StoppelMapDatabase>().localized_stringQueries,
            dataStore = PreferenceDataStoreFactory.createWithPath(
                corruptionHandler = null,
                migrations = emptyList(),
                produceFile = { get<PreferencesPathFactory>().create("scheduleUserData").toPath() },
            )
        )
    }

    factory {
        GetScheduleDaysUseCase(
            eventRepository = get()
        )
    }

    factory {
        GetBookmarkedEventsUseCase(
            eventRepository = get(),
            clockProvider = get(),
        )
    }
}
