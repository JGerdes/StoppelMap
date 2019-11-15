package com.jonasgerdes.stoppelmap.transport.view.route

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonasgerdes.stoppelmap.transport.usecase.GetFullRouteUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class RouteDetailViewModel @Inject constructor(
    private val getFullRoute: GetFullRouteUseCase
) : ViewModel() {

    private val _route = MutableLiveData<GetFullRouteUseCase.FullRoute>()
    val route: LiveData<GetFullRouteUseCase.FullRoute> = _route

    private var routeJob: Job? = null

    fun setRoute(routeSlug: String) {
        routeJob?.cancel()
        routeJob = viewModelScope.launch {
            val route = getFullRoute(routeSlug)
            _route.postValue(route)
        }
    }

}