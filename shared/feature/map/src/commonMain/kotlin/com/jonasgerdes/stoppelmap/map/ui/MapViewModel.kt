package com.jonasgerdes.stoppelmap.map.ui

import co.touchlab.skie.configuration.annotations.DefaultArgumentInterop
import com.rickclephas.kmm.viewmodel.KMMViewModel
import com.rickclephas.kmm.viewmodel.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf

class MapViewModel(
) : KMMViewModel() {


    val state: StateFlow<ViewState> = flowOf<ViewState>()
        .stateIn(
            viewModelScope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = ViewState()
        )

    data class ViewState
    @DefaultArgumentInterop.Enabled
    constructor(
        val dummy: Unit = Unit,
    )
}