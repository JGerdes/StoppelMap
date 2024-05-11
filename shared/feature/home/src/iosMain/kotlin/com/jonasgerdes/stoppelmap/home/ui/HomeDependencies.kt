package com.jonasgerdes.stoppelmap.home.ui

import com.jonasgerdes.stoppelmap.countdown.usecase.GetOpeningCountDownStateUseCase
import com.jonasgerdes.stoppelmap.countdown.usecase.ShouldShowCountdownWidgetSuggestionUseCase
import com.jonasgerdes.stoppelmap.home.usecase.GetRemoteMessagesUseCase
import com.jonasgerdes.stoppelmap.schedule.usecase.GetNextBookmarkedEventUseCase
import com.jonasgerdes.stoppelmap.schedule.usecase.GetNextOfficialEventUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HomeDependencies : KoinComponent {
    val getOpeningCountDownState: GetOpeningCountDownStateUseCase by inject()
    val shouldShowCountdownWidgetSuggestion: ShouldShowCountdownWidgetSuggestionUseCase by inject()
    val getNextOfficialEvent: GetNextOfficialEventUseCase by inject()
    val getNextBookmarkedEvent: GetNextBookmarkedEventUseCase by inject()
    val getRemoteMessages: GetRemoteMessagesUseCase by inject()
}