package com.jonasgerdes.stoppelmap.data

import org.koin.dsl.module

val dataModule = module {
    single {
        StoppelMapDatabase(
            driver = get(), eventAdapter = Event.Adapter(
                startAdapter = localDateTimeAdapter,
                endAdapter = localDateTimeAdapter
            )
        )
    }
}
