package com.jonasgerdes.stoppelmap.data

import org.koin.dsl.module

val dataModule = module {
    single<StoppelmapDatabase> { StoppelmapDatabase.getInstance(context = get()) }
}