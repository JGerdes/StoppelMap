@file:OptIn(ExperimentalCoroutinesApi::class)

package com.jonasgerdes.stoppelmap.transportation.ui.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonasgerdes.stoppelmap.transportation.data.BusRoutesRepository
import com.jonasgerdes.stoppelmap.transportation.data.TransportationUserDataRepository
import com.jonasgerdes.stoppelmap.transportation.model.BusRouteDetails
import com.jonasgerdes.stoppelmap.transportation.model.RouteSummary
import com.jonasgerdes.stoppelmap.transportation.usecase.GetNextDeparturesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class TransportationOverviewViewModel(
    busRoutesRepository: BusRoutesRepository,
    transportationUserDataRepository: TransportationUserDataRepository,
    private val getNextDepartures: GetNextDeparturesUseCase,
) : ViewModel() {

    private val timeUpdate = flow {
        while (true) {
            emit(Unit)
            delay(10_000)
        }
    }

    private val busRoutesState = busRoutesRepository.getAllRoutes()
        .map { BusRoutesState.Loaded(it) as BusRoutesState }
        .onStart { emit(BusRoutesState.Loading) }

    private val favouriteState = transportationUserDataRepository.getFavouriteStations()
        .flatMapLatest { favs ->
            timeUpdate.map {
                FavouriteState.Loaded(
                    favs.map { stationId ->
                        val station = busRoutesRepository.getStationById(stationId).first()
                        BusRouteDetails.Station.Stop(
                            id = station.id,
                            title = station.title,
                            nextDepartures = getNextDepartures(station.departures.flatMap { it.departures })
                        )
                    }
                ) as FavouriteState
            }
        }.onStart { emit(FavouriteState.Loading) }

    val state: StateFlow<ViewState> =
        combine(
            busRoutesState,
            favouriteState,
            ::ViewState
        )
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = ViewState()
            )

    data class ViewState(
        val busRoutesViewState: BusRoutesState = BusRoutesState.Loading,
        val favouriteState: FavouriteState = FavouriteState.Loading
    )

    sealed interface BusRoutesState {
        object Loading : BusRoutesState
        data class Loaded(val routes: List<RouteSummary>) : BusRoutesState
    }

    sealed interface FavouriteState {
        object Loading : FavouriteState
        data class Loaded(val favouriteStations: List<BusRouteDetails.Station.Stop>) :
            FavouriteState
    }
}
