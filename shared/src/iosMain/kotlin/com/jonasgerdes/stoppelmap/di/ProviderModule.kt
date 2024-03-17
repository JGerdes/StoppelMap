package com.jonasgerdes.stoppelmap.di

import com.jonasgerdes.stoppelmap.base.contract.ClockProvider
import com.jonasgerdes.stoppelmap.base.contract.SeasonProvider
import com.jonasgerdes.stoppelmap.provider.StoppelmarktClockProvider
import com.jonasgerdes.stoppelmap.provider.StoppelmarktSeasonProvider
import org.koin.dsl.module

val dateTimeModule = module {
    single<ClockProvider> { StoppelmarktClockProvider() }
    single<SeasonProvider> { StoppelmarktSeasonProvider(clockProvider = get()) }
}