package com.jonasgerdes.stoppelmap.home.ui

import com.jonasgerdes.stoppelmap.countdown.usecase.GetOpeningCountDownStateUseCase
import com.jonasgerdes.stoppelmap.countdown.usecase.ShouldShowCountdownWidgetSuggestionUseCase
import com.jonasgerdes.stoppelmap.home.usecase.GetPromotedEventsUseCase
import com.jonasgerdes.stoppelmap.home.usecase.GetRemoteMessagesUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HomeDependencies : KoinComponent {
    val getOpeningCountDownState: GetOpeningCountDownStateUseCase by inject()
    val shouldShowCountdownWidgetSuggestion: ShouldShowCountdownWidgetSuggestionUseCase by inject()
    val getPromotedEventsUseCase: GetPromotedEventsUseCase by inject()
    val getRemoteMessages: GetRemoteMessagesUseCase by inject()
}