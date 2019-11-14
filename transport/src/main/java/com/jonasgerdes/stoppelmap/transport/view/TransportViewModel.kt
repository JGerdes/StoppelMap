package com.jonasgerdes.stoppelmap.transport.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonasgerdes.stoppelmap.transport.entity.BusRoute
import com.jonasgerdes.stoppelmap.transport.entity.TransportOptions
import com.jonasgerdes.stoppelmap.transport.usecase.GetRoutesUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class TransportViewModel @Inject constructor(
    private val getRoutes: GetRoutesUseCase
) : ViewModel() {

    private val _transportOptions = MutableLiveData<TransportOptions>()
    val transportOptions: LiveData<TransportOptions> = _transportOptions

    init {
        viewModelScope.launch {
            val busRoutes = getRoutes().map { route ->
                BusRoute(
                    title = route.basicInfo.name,
                    slug = route.basicInfo.slug,
                    via = route.via.map { it.name }
                )
            }

            _transportOptions.postValue(
                TransportOptions(
                    bus = busRoutes
                )
            )
        }
    }

}