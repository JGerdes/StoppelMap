package com.jonasgerdes.stoppelmap.home.ui

import com.jonasgerdes.stoppelmap.countdown.usecase.ShouldShowCountdownWidgetSuggestionUseCase
import com.jonasgerdes.stoppelmap.countdown.usecase.ShouldShowCountdownWidgetSuggestionUseCaseImpl
import org.koin.dsl.module

val iosCountdownModule = module {
    factory<ShouldShowCountdownWidgetSuggestionUseCase> {
        ShouldShowCountdownWidgetSuggestionUseCaseImpl()
    }
}