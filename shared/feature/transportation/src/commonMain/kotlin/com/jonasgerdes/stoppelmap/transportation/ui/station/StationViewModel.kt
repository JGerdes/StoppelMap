package com.jonasgerdes.stoppelmap.transportation.ui.station

import co.touchlab.skie.configuration.annotations.DefaultArgumentInterop
import com.jonasgerdes.stoppelmap.transportation.data.BusRoutesRepository
import com.jonasgerdes.stoppelmap.transportation.data.TransportationUserDataRepository
import com.jonasgerdes.stoppelmap.transportation.model.StationDetails
import com.jonasgerdes.stoppelmap.transportation.model.Timetable
import com.jonasgerdes.stoppelmap.transportation.usecase.CreateTimetableUseCase
import com.rickclephas.kmm.viewmodel.KMMViewModel
import com.rickclephas.kmm.viewmodel.coroutineScope
import com.rickclephas.kmm.viewmodel.stateIn
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class StationViewModel(
    private val stationId: String,
    busRoutesRepository: BusRoutesRepository,
    private val transportationUserDataRepository: TransportationUserDataRepository,
    createTimetable: CreateTimetableUseCase
) : KMMViewModel() {

    private val stationState = busRoutesRepository.getStationDetails(stationId)

    private val timeUpdate = flow {
        while (true) {
            emit(Unit)
            delay(30.seconds)
        }
    }

    val state: StateFlow<ViewState> =
        combine(
            stationState,
            transportationUserDataRepository.getFavouriteStations(),
            timeUpdate,
        ) { station, favoriteStations, timeTick ->
            StationState.Loaded(
                stationName = station.name,
                outwardTimetable = createTimetable(station.outwardDepartures),
                returnTimetable = createTimetable(station.returnDepartures),
                priceState = PriceState(prices = station.fees, showDeutschlandTicketHint = true),
                additionalInfo = station.additionalInfo,
                isFavourite = favoriteStations.contains(station.slug),
            )
        }
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
            val stationName: String,
            val outwardTimetable: Timetable,
            val returnTimetable: Timetable,
            val priceState: PriceState,
            val additionalInfo: String?,
            val isFavourite: Boolean,
        ) : StationState()
    }

    data class PriceState(
        val prices: List<StationDetails.Fee>,
        val showDeutschlandTicketHint: Boolean
    )

}
