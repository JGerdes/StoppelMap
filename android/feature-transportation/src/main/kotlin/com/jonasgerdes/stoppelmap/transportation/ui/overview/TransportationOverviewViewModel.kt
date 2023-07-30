package com.jonasgerdes.stoppelmap.transportation.ui.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonasgerdes.stoppelmap.transportation.data.BusRoutesRepository
import com.jonasgerdes.stoppelmap.transportation.model.RouteSummary
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class TransportationOverviewViewModel(
    busRoutesRepository: BusRoutesRepository
) : ViewModel() {

    private val busRoutesState = busRoutesRepository.getAllRoutes().map(BusRoutesState::Loaded)

    val state: StateFlow<ViewState> =
        busRoutesState
            .map(::ViewState)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = ViewState.Default
            )

    data class ViewState(
        val busRoutesViewState: BusRoutesState,
    ) {

        companion object {
            val Default = ViewState(
                busRoutesViewState = BusRoutesState.Loading
            )
        }
    }

    sealed class BusRoutesState {
        object Loading : BusRoutesState()
        data class Loaded(val routes: List<RouteSummary>) : BusRoutesState()
    }
}
