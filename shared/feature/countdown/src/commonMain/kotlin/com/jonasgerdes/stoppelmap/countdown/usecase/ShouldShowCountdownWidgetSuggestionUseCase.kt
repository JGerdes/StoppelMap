package com.jonasgerdes.stoppelmap.countdown.usecase

interface ShouldShowCountdownWidgetSuggestionUseCase {

    suspend operator fun invoke(): Boolean
}