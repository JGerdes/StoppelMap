package com.jonasgerdes.stoppelmap.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonasgerdes.stoppelmap.countdown.model.CountDown
import com.jonasgerdes.stoppelmap.countdown.usecase.GetOpeningCountDownFlowUseCase
import com.jonasgerdes.stoppelmap.countdown.usecase.ShouldShowCountdownWidgetSuggestionUseCase
import kotlinx.coroutines.flow.*

class HomeViewModel(
    getOpeningCountDown: GetOpeningCountDownFlowUseCase,
    private val shouldShowCountdownWidgetSuggestion: ShouldShowCountdownWidgetSuggestionUseCase,
) : ViewModel() {

    private val openingCountDownState: Flow<CountDownState> =
        getOpeningCountDown().map { countDownResult ->
            if (countDownResult is CountDown.InFuture) {
                CountDownState.CountingDown(
                    daysLeft = countDownResult.daysLeft,
                    hoursLeft = countDownResult.hoursLeft,
                    minutesLeft = countDownResult.minutesLeft
                )
            } else {
                CountDownState.Over
            }
        }

    private val countdownWidgetSuggestionState: Flow<CountDownWidgetSuggestionState> = flow {
        emit(
            when (shouldShowCountdownWidgetSuggestion()) {
                true -> CountDownWidgetSuggestionState.Visible
                false -> CountDownWidgetSuggestionState.Hidden
            }
        )
    }

    val state: StateFlow<ViewState> =
        combine(
            openingCountDownState,
            countdownWidgetSuggestionState,
            ::ViewState
        ).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = ViewState.Default
        )

    data class ViewState(
        val openingCountDownState: CountDownState,
        val countdownWidgetSuggestionState: CountDownWidgetSuggestionState,
    ) {
        companion object {
            val Default = ViewState(
                openingCountDownState = CountDownState.Loading,
                countdownWidgetSuggestionState = CountDownWidgetSuggestionState.Hidden
            )
        }
    }

    sealed class CountDownWidgetSuggestionState {
        object Hidden : CountDownWidgetSuggestionState()
        object Visible : CountDownWidgetSuggestionState()
    }

    sealed class CountDownState {
        object Loading : CountDownState()
        data class CountingDown(
            val daysLeft: Int, val hoursLeft: Int, val minutesLeft: Int
        ) : CountDownState()

        object Over : CountDownState()
    }
}
