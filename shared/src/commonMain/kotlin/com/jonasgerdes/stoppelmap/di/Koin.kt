package com.jonasgerdes.stoppelmap.di

import co.touchlab.skie.configuration.annotations.DefaultArgumentInterop
import com.jonasgerdes.stoppelmap.greeting.Greeting
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module


val greetingModule = module {
    single {
        Greeting("Hallo Stoppelmarkt!")
    }
}

@DefaultArgumentInterop.Enabled
fun initKoin(modules: List<Module> = emptyList()) {
    startKoin {
        modules(
            *modules.toTypedArray(),
            greetingModule,
        )
    }
}
