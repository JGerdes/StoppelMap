package com.jonasgerdes.stoppelmap.server.scheduler

import org.koin.dsl.module

val schedulerModule = module {
    single {
        TaskScheduler(
            tasks = getAll<Task>(),
            clockProvider = get(),
            logger = get(),
            monitoring = get()
        )
    }
}