@file:OptIn(ExperimentalCoroutinesApi::class)

package com.jonasgerdes.stoppelmap.shared.dataupdate.ui

import co.touchlab.skie.configuration.annotations.DefaultArgumentInterop
import com.jonasgerdes.stoppelmap.dto.config.Notice
import com.jonasgerdes.stoppelmap.shared.dataupdate.usecase.GetDataNoticeUseCase
import com.rickclephas.kmm.viewmodel.KMMViewModel
import com.rickclephas.kmm.viewmodel.stateIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

class DataNoticeViewModel(
    private val getDataNotice: GetDataNoticeUseCase,
) : KMMViewModel() {
    val state: StateFlow<ViewState> =
        getDataNotice()
            .map { ViewState(it) }
            .stateIn(
                viewModelScope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = ViewState()
            )

    data class ViewState
    @DefaultArgumentInterop.Enabled
    constructor(
        val notices: List<Notice> = emptyList(),
    )
}
