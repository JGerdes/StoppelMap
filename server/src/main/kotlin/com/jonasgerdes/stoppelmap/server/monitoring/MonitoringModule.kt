package com.jonasgerdes.stoppelmap.server.monitoring

import com.jonasgerdes.stoppelmap.server.scheduler.Task
import org.koin.dsl.bind
import org.koin.dsl.module

val monitoringModule = module {
    single { Monitoring(get()) }
    single {
        InitMetricsTask(
            articleRepository = get(),
            monitoring = get()
        )
    } bind Task::class
}