package com.jonasgerdes.stoppelmap.di

import co.touchlab.skie.configuration.annotations.DefaultArgumentInterop
import org.koin.core.context.startKoin
import org.koin.core.module.Module


@DefaultArgumentInterop.Enabled
fun initKoin(modules: List<Module> = emptyList()) {
    startKoin {
        modules(
            *modules.toTypedArray(),
        )
    }
}
