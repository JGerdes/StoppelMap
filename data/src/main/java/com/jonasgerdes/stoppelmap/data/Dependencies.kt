package com.jonasgerdes.stoppelmap.data

import com.jonasgerdes.stoppelmap.data.repository.EventRepository
import com.jonasgerdes.stoppelmap.data.repository.RouteRepository
import com.jonasgerdes.stoppelmap.data.repository.StallRepository
import org.koin.dsl.module

val dataModule = module {
    single<StoppelmapDatabase> { RoomStoppelmapDatabase.getInstance(context = get()) }

    single<StallRepository> { StallRepository(database = get()) }
    single<EventRepository> { EventRepository(database = get()) }
    single<RouteRepository> { RouteRepository(database = get()) }
}