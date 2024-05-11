package com.jonasgerdes.stoppelmap.di

import co.touchlab.skie.configuration.annotations.DefaultArgumentInterop
import com.jonasgerdes.stoppelmap.countdown.countDownModule
import com.jonasgerdes.stoppelmap.data.dataModule
import com.jonasgerdes.stoppelmap.schedule.scheduleModule
import com.jonasgerdes.stoppelmap.shared.dataupdate.dataUpdateModule
import com.jonasgerdes.stoppelmap.transportation.transportationModule
import com.jonasgerdes.stoppelmap.venue.venueModule
import homeModule
import org.koin.core.context.startKoin
import org.koin.core.module.Module


@DefaultArgumentInterop.Enabled
fun initKoin(modules: List<Module> = emptyList()) {
    startKoin {
        modules(
            *modules.toTypedArray(),
            commonAppModule,
            dataModule,
            venueModule,
            dataUpdateModule,
            homeModule,
            countDownModule,
            scheduleModule,
            transportationModule,
        )
    }
}
