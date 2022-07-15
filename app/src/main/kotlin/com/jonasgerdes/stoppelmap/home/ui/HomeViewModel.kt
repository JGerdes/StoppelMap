package com.jonasgerdes.stoppelmap.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonasgerdes.stoppelmap.countdown.model.CountDown
import com.jonasgerdes.stoppelmap.countdown.usecase.GetOpeningCountDownFlowUseCase
import kotlinx.coroutines.flow.*

class HomeViewModel(
    getOpeningCountDown: GetOpeningCountDownFlowUseCase
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

    val state: StateFlow<ViewState> = openingCountDownState
        .map(::ViewState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = ViewState.Default
        )

    data class ViewState(
        val openingCountDownState: CountDownState
    ) {
        companion object {
            val Default = ViewState(
                openingCountDownState = CountDownState.Loading
            )
        }
    }

    sealed class CountDownState {
        object Loading : CountDownState()
        data class CountingDown(
            val daysLeft: Int, val hoursLeft: Int, val minutesLeft: Int
        ) : CountDownState()

        object Over : CountDownState()
    }
}
