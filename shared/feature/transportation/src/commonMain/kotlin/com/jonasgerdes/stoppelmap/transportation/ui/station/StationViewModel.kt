package com.jonasgerdes.stoppelmap.transportation.ui.station

import co.touchlab.skie.configuration.annotations.DefaultArgumentInterop
import com.jonasgerdes.stoppelmap.transportation.data.BusRoutesRepository
import com.jonasgerdes.stoppelmap.transportation.data.TransportationUserDataRepository
import com.jonasgerdes.stoppelmap.transportation.model.Timetable
import com.jonasgerdes.stoppelmap.transportation.usecase.CreateTimetableUseCase
import com.rickclephas.kmm.viewmodel.KMMViewModel
import com.rickclephas.kmm.viewmodel.coroutineScope
import com.rickclephas.kmm.viewmodel.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class StationViewModel(
    private val stationId: String,
    busRoutesRepository: BusRoutesRepository,
    private val transportationUserDataRepository: TransportationUserDataRepository,
    createTimetable: CreateTimetableUseCase
) : KMMViewModel() {

    private val routeState = flowOf(StationState.Loading)

    val state: StateFlow<ViewState> =
        routeState
            .map(StationViewModel::ViewState)
            .stateIn(
                viewModelScope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = ViewState()
            )

    data class ViewState
    @DefaultArgumentInterop.Enabled
    constructor(
        val stationState: StationState = StationState.Loading,
    )

    fun toggleFavourite() {
        val isFavourite = (state.value.stationState as? StationState.Loaded)?.isFavourite == true
        viewModelScope.coroutineScope.launch {
            if (!isFavourite) {
                transportationUserDataRepository.addFavouriteStations(stationId)
            } else {
                transportationUserDataRepository.removeFavouriteStations(stationId)
            }
        }
    }

    sealed class StationState {
        object Loading : StationState()
        data class Loaded(
            val stationTitle: String,
            val timetable: Timetable,
            val priceState: PriceState,
            val isFavourite: Boolean,
        ) : StationState()
    }

    data class PriceState(
        val prices: List<Unit>,
        val showDeutschlandTicketHint: Boolean
    )

}
