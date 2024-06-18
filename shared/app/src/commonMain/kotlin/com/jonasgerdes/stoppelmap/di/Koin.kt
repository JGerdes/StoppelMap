package com.jonasgerdes.stoppelmap.di

import co.touchlab.skie.configuration.annotations.DefaultArgumentInterop
import com.jonasgerdes.stoppelmap.countdown.countDownModule
import com.jonasgerdes.stoppelmap.data.dataModule
import com.jonasgerdes.stoppelmap.news.newsModule
import com.jonasgerdes.stoppelmap.schedule.scheduleModule
import com.jonasgerdes.stoppelmap.shared.dataupdate.dataUpdateModule
import com.jonasgerdes.stoppelmap.shared.network.networkModule
import com.jonasgerdes.stoppelmap.transportation.transportationModule
import com.jonasgerdes.stoppelmap.venue.venueModule
import homeModule
import licensesModule
import org.koin.core.context.startKoin
import org.koin.core.module.Module


@DefaultArgumentInterop.Enabled
fun initKoin(modules: List<Module> = emptyList()) {
    startKoin {
        modules(
            *modules.toTypedArray(),
            networkModule,
            commonAppModule,
            dataModule,
            venueModule,
            dataUpdateModule,
            homeModule,
            licensesModule,
            countDownModule,
            scheduleModule,
            transportationModule,
            newsModule,
        )
    }
}
