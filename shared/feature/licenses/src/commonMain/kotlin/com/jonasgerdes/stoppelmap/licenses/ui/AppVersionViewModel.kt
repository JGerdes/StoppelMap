package com.jonasgerdes.stoppelmap.licenses.ui

import co.touchlab.skie.configuration.annotations.DefaultArgumentInterop
import com.jonasgerdes.stoppelmap.shared.dataupdate.usecase.GetCurrentDataVersionUseCase
import com.rickclephas.kmm.viewmodel.KMMViewModel
import com.rickclephas.kmm.viewmodel.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

class AppVersionViewModel(
    private val getCurrentDataVersion: GetCurrentDataVersionUseCase
) : KMMViewModel() {

    val state: StateFlow<ViewState> = getCurrentDataVersion().map { ViewState(it.toString()) }
        .stateIn(
            viewModelScope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = ViewState()
        )

    data class ViewState
    @DefaultArgumentInterop.Enabled
    constructor(
        val dataVersion: String = "???",
    )
}