@file:OptIn(ExperimentalCoroutinesApi::class)

package com.jonasgerdes.stoppelmap.home.ui

import co.touchlab.skie.configuration.annotations.DefaultArgumentInterop
import com.jonasgerdes.stoppelmap.countdown.model.CountDownState
import com.jonasgerdes.stoppelmap.countdown.usecase.GetOpeningCountDownStateUseCase
import com.jonasgerdes.stoppelmap.countdown.usecase.ShouldShowCountdownWidgetSuggestionUseCase
import com.jonasgerdes.stoppelmap.data.Event
import com.jonasgerdes.stoppelmap.dto.config.Message
import com.jonasgerdes.stoppelmap.home.usecase.GetRemoteMessagesUseCase
import com.jonasgerdes.stoppelmap.schedule.usecase.GetNextBookmarkedEventUseCase
import com.jonasgerdes.stoppelmap.schedule.usecase.GetNextOfficialEventUseCase
import com.rickclephas.kmm.viewmodel.KMMViewModel
import com.rickclephas.kmm.viewmodel.stateIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlin.time.Duration.Companion.minutes

class HomeViewModel(
    getOpeningCountDownState: GetOpeningCountDownStateUseCase,
    private val shouldShowCountdownWidgetSuggestion: ShouldShowCountdownWidgetSuggestionUseCase,
    getNextOfficialEvent: GetNextOfficialEventUseCase,
    getNextBookmarkedEvent: GetNextBookmarkedEventUseCase,
    getRemoteMessages: GetRemoteMessagesUseCase,
) : KMMViewModel() {

    private val countdownWidgetSuggestionState: Flow<CountDownWidgetSuggestionState> = flow {
        emit(
            when (shouldShowCountdownWidgetSuggestion()) {
                true -> CountDownWidgetSuggestionState.Visible
                false -> CountDownWidgetSuggestionState.Hidden
            }
        )
    }

    private val promotedEventsState = flow {
        emit(Unit)
        while (true) {
            delay(1.minutes)
            emit(Unit)
        }
    }.flatMapLatest {
        getNextBookmarkedEvent().map { bookmarked ->
            val official =
                (getNextOfficialEvent() as? GetNextOfficialEventUseCase.Result.Some)?.event

            val promotedEvents = (bookmarked + official)
                .filterNotNull()
                .distinctBy { it.slug }
                .sortedBy { it.start }
            if (promotedEvents.isEmpty()) {
                PromotedEventsState.Hidden
            } else {
                PromotedEventsState.Visible(events = promotedEvents)
            }
        }
    }

    private val instagramPromotionState = getOpeningCountDownState().map {
        when (it) {
            is CountDownState.Loading -> InstagramPromotionState.Hidden
            else -> InstagramPromotionState.Visible
        }
    }

    val state: StateFlow<ViewState> =
        combine(
            getRemoteMessages().onStart { emit(emptyList()) },
            getOpeningCountDownState(),
            countdownWidgetSuggestionState,
            promotedEventsState,
            instagramPromotionState,
            ::ViewState
        ).stateIn(
            viewModelScope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = ViewState()
        )

    data class ViewState
    @DefaultArgumentInterop.Enabled
    constructor(
        val messages: List<Message> = emptyList(),
        val openingCountDownState: CountDownState = CountDownState.Loading,
        val countdownWidgetSuggestionState: CountDownWidgetSuggestionState = CountDownWidgetSuggestionState.Hidden,
        val promotedEventsState: PromotedEventsState = PromotedEventsState.Loading,
        val instagramPromotionState: InstagramPromotionState = InstagramPromotionState.Hidden,
    )

    sealed class CountDownWidgetSuggestionState {
        object Hidden : CountDownWidgetSuggestionState()
        object Visible : CountDownWidgetSuggestionState()
    }

    sealed interface PromotedEventsState {
        object Loading : PromotedEventsState

        object Hidden : PromotedEventsState

        data class Visible(
            val events: List<Event>
        ) : PromotedEventsState
    }

    sealed interface InstagramPromotionState {
        object Hidden : InstagramPromotionState
        object Visible : InstagramPromotionState
    }
}
