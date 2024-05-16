@file:OptIn(ExperimentalCoroutinesApi::class)

package com.jonasgerdes.stoppelmap.licenses.ui

import co.touchlab.skie.configuration.annotations.DefaultArgumentInterop
import com.jonasgerdes.stoppelmap.licenses.data.LicensesRepository
import com.jonasgerdes.stoppelmap.licenses.model.ImageSource
import com.jonasgerdes.stoppelmap.licenses.model.Library
import com.rickclephas.kmm.viewmodel.KMMViewModel
import com.rickclephas.kmm.viewmodel.MutableStateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LicensesViewModel(
    private val licensesRepository: LicensesRepository,
) : KMMViewModel() {

    private val stateFlow = MutableStateFlow(viewModelScope, ViewState())

    val state: StateFlow<ViewState> = stateFlow.asStateFlow()

    init {
        stateFlow.value = ViewState(
            libraries = licensesRepository.getLibraries(),
            images = licensesRepository.getImageSources(),
        )
    }

    data class ViewState
    @DefaultArgumentInterop.Enabled
    constructor(
        val libraries: List<Library> = emptyList(),
        val images: List<ImageSource> = emptyList(),
    )
}
