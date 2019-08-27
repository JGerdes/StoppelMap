package com.jonasgerdes.stoppelmap.transport.view.station

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonasgerdes.stoppelmap.transport.usecase.GetFullStationUseCase
import kotlinx.coroutines.launch

class StationDetailViewModel(
    private val stationSlug: String,
    private val getFullStation: GetFullStationUseCase
) : ViewModel() {

    private val _station = MutableLiveData<GetFullStationUseCase.FullStation>()
    val station: LiveData<GetFullStationUseCase.FullStation> = _station

    init {
        viewModelScope.launch {
            val station = getFullStation(stationSlug)
            _station.postValue(station)
        }
    }
}