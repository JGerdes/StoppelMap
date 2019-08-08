package com.jonasgerdes.stoppelmap.data

import com.jonasgerdes.stoppelmap.data.repository.EventRepository
import com.jonasgerdes.stoppelmap.data.repository.StallRepository
import org.koin.dsl.module

val dataModule = module {
    single<StoppelmapDatabase> { StoppelmapDatabase.getInstance(context = get()) }

    single<StallRepository> { StallRepository(database = get()) }
    single<EventRepository> { EventRepository(database = get()) }
}