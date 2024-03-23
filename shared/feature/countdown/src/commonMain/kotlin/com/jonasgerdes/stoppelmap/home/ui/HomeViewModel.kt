package com.jonasgerdes.stoppelmap.home.ui

import co.touchlab.skie.configuration.annotations.DefaultArgumentInterop
import com.jonasgerdes.stoppelmap.countdown.model.CountDownState
import com.jonasgerdes.stoppelmap.countdown.usecase.GetOpeningCountDownStateUseCase
import com.rickclephas.kmm.viewmodel.KMMViewModel
import com.rickclephas.kmm.viewmodel.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

class HomeViewModel2(
    private val getOpeningCountDownState: GetOpeningCountDownStateUseCase,
) : KMMViewModel() {

    val state: StateFlow<ViewState> = getOpeningCountDownState()
        .map(::ViewState)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ViewState())

    data class ViewState
    @DefaultArgumentInterop.Enabled
    constructor(val countDownState: CountDownState = CountDownState.Loading)
}