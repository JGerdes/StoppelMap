package com.jonasgerdes.stoppelmap.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonasgerdes.stoppelmap.home.usecase.GetOpeningCountDownUseCase
import kotlinx.coroutines.flow.*
import timber.log.Timber

class HomeViewModel(
    getOpeningCountDown: GetOpeningCountDownUseCase
) : ViewModel() {


    private val openingCountDownState: Flow<CountDownState> =
        getOpeningCountDown().map { countDownResult ->
            Timber.d("new countDownResult: $countDownResult")
            if (countDownResult.isOver) {
                CountDownState.Over
            } else {
                CountDownState.CountingDown(
                    daysLeft = countDownResult.daysLeft,
                    hoursLeft = countDownResult.hoursLeft,
                    minutesLeft = countDownResult.minutesLeft
                )
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
