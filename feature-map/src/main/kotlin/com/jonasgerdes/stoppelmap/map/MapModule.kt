package com.jonasgerdes.stoppelmap.map


import com.jonasgerdes.stoppelmap.map.usecase.InitializeMapBoxUseCase
import org.koin.dsl.module

val mapModule = module {
    single { InitializeMapBoxUseCase() }
}
