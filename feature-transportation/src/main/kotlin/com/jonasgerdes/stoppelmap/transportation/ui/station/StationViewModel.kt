package com.jonasgerdes.stoppelmap.transportation.ui.station

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonasgerdes.stoppelmap.transportation.data.BusRoutesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class StationViewModel(
    stationId: String,
    busRoutesRepository: BusRoutesRepository
) : ViewModel() {

    private val routeState = busRoutesRepository.getStationById(stationId)
        .map { station ->
            StationState.Loaded(
                stationTitle = station.title
            )
        }

    val state: StateFlow<ViewState> =
        routeState
            .map(StationViewModel::ViewState)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = ViewState.Default
            )

    data class ViewState(
        val stationState: StationState,
    ) {

        companion object {
            val Default = ViewState(
                stationState = StationState.Loading
            )
        }
    }

    sealed class StationState {
        object Loading : StationState()
        data class Loaded(
            val stationTitle: String,
        ) : StationState()
    }
}
