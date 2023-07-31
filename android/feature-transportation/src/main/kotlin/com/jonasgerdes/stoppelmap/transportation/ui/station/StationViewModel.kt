package com.jonasgerdes.stoppelmap.transportation.ui.station

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonasgerdes.stoppelmap.transportation.data.BusRoutesRepository
import com.jonasgerdes.stoppelmap.transportation.data.TransportationUserDataRepository
import com.jonasgerdes.stoppelmap.transportation.model.Price
import com.jonasgerdes.stoppelmap.transportation.model.Station
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
                        timetable = createTimetable(it.departures)

                    )
                },
            transportationUserDataRepository.getFavouriteStations().onStart { emit(emptySet()) },
        )
        { stationWithTimetable, favouriteStations ->
            val (station, timetable) = stationWithTimetable
            StationState.Loaded(
                stationTitle = station.title,
                timetable = timetable,
                prices = station.prices,
                isFavourite = favouriteStations.contains(station.id)
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
            val prices: List<Price>,
            val isFavourite: Boolean,
        ) : StationState()
    }

    private data class StationWithTimetable(
        val station: Station,
        val timetable: Timetable,
    )
}
