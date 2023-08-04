package com.jonasgerdes.stoppelmap.transportation.ui.station

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonasgerdes.stoppelmap.data.model.database.RouteType
import com.jonasgerdes.stoppelmap.transportation.data.BusRoutesRepository
import com.jonasgerdes.stoppelmap.transportation.data.TransportationUserDataRepository
import com.jonasgerdes.stoppelmap.transportation.model.ExtendedStation
import com.jonasgerdes.stoppelmap.transportation.model.Price
import com.jonasgerdes.stoppelmap.transportation.model.Timetable
import com.jonasgerdes.stoppelmap.transportation.usecase.CreateTimetableUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StationViewModel(
    private val stationId: String,
    busRoutesRepository: BusRoutesRepository,
    private val transportationUserDataRepository: TransportationUserDataRepository,
    createTimetable: CreateTimetableUseCase
) : ViewModel() {

    private val routeState =
        combine(
            busRoutesRepository.getStationById(stationId)
                .map {
                    StationWithTimetable(
                        station = it,
                        timetable = createTimetable(it.station.departures)

                    )
                },
            transportationUserDataRepository.getFavouriteStations().onStart { emit(emptySet()) },
        )
        { stationWithTimetable, favouriteStations ->
            val (extendedStation, timetable) = stationWithTimetable
            StationState.Loaded(
                stationTitle = extendedStation.station.title,
                timetable = timetable,
                priceState = PriceState(
                    prices = extendedStation.station.prices,
                    showDeutschlandTicketHint = extendedStation.routeType == RouteType.Bus,
                ),
                isFavourite = favouriteStations.contains(extendedStation.station.id)
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

    fun toggleFavourite() {
        val isFavourite = (state.value.stationState as? StationState.Loaded)?.isFavourite == true
        viewModelScope.launch {
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
        val prices: List<Price>,
        val showDeutschlandTicketHint: Boolean
    )

    private data class StationWithTimetable(
        val station: ExtendedStation,
        val timetable: Timetable,
    )
}
