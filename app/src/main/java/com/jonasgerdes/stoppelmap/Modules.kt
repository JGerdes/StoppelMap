package com.jonasgerdes.stoppelmap

import com.jonasgerdes.stoppelmap.core.domain.DateTimeProvider
import com.jonasgerdes.stoppelmap.core.domain.LiveDateTimeProvider
import org.koin.dsl.module


val appModule = module {
    single<DateTimeProvider> { LiveDateTimeProvider() }
}