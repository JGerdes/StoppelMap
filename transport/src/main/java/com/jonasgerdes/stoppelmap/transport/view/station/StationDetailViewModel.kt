package com.jonasgerdes.stoppelmap.transport.view.station

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonasgerdes.stoppelmap.transport.usecase.GetFullStationUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class StationDetailViewModel @Inject constructor(
    private val getFullStation: GetFullStationUseCase
) : ViewModel() {

    private val _station = MutableLiveData<GetFullStationUseCase.FullStation>()
    val station: LiveData<GetFullStationUseCase.FullStation> = _station

    private var stationJob: Job? = null

    fun setStation(stationSlug: String) {
        stationJob?.cancel()
        stationJob = viewModelScope.launch {
            val station = getFullStation(stationSlug)
            _station.postValue(station)
        }
    }
}