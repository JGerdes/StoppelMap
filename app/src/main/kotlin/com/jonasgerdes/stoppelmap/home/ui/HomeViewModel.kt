package com.jonasgerdes.stoppelmap.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonasgerdes.stoppelmap.home.usecase.GetOpeningCountDownUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getOpeningCountDown: GetOpeningCountDownUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ViewState.Default)
    val state: StateFlow<ViewState> = _state

    init {
        viewModelScope.launch {
            getOpeningCountDown().collect { countDownResult ->
                updateState {
                    copy(
                        openingCountDownState = if (countDownResult.isOver) {
                            CountDownState.Over
                        } else {
                            CountDownState.CountingDown(
                                daysLeft = countDownResult.daysLeft,
                                hoursLeft = countDownResult.hoursLeft,
                                minutesLeft = countDownResult.minutesLeft
                            )
                        }
                    )
                }
            }
        }
    }

    private fun updateState(updater: ViewState.() -> ViewState) {
        _state.value = _state.value.updater()
    }

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
