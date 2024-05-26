package com.jonasgerdes.stoppelmap.countdown.usecase

class ShouldShowCountdownWidgetSuggestionUseCaseImpl : ShouldShowCountdownWidgetSuggestionUseCase {
    override suspend fun invoke(): Boolean {
        return false
    }
}