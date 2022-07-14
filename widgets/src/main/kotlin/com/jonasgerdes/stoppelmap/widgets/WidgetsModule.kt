package com.jonasgerdes.stoppelmap.widgets

import com.jonasgerdes.stoppelmap.home.usecase.GetTimeLeftToOpeningUseCase
import org.koin.dsl.module


val widgetsModule = module {

    single { GingerbreadWidgetSettings(context = get()) }

    factory { GetTimeLeftToOpeningUseCase() }
}
