package com.jonasgerdes.stoppelmap.map

import com.jonasgerdes.stoppelmap.map.usecase.GetMapFilePathUseCase
import org.koin.dsl.module

val mapModule = module {

    factory {
        GetMapFilePathUseCase(
            appUpdateRepository = get()
        )
    }
}
