package com.jonasgerdes.stoppelmap.transportation.ui.station

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonasgerdes.stoppelmap.transportation.data.BusRoutesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class StationViewModel(
    stationId: String,
    busRoutesRepository: BusRoutesRepository
) : ViewModel() {


    val state: StateFlow<ViewState> =
        flow<ViewState> { }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = ViewState.Default
            )

    class ViewState(

    ) {

        companion object {
            val Default = ViewState(

            )
        }
    }
}
