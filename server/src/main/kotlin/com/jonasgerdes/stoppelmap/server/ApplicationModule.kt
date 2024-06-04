package com.jonasgerdes.stoppelmap.server

import com.jonasgerdes.stoppelmap.server.scheduler.ClockProvider
import kotlinx.datetime.Clock
import org.koin.dsl.module

val applicationModule = module {
    single<ClockProvider> { ClockProvider { Clock.System.now() } }
}