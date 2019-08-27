package com.jonasgerdes.stoppelmap.transport.view.route

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonasgerdes.stoppelmap.transport.usecase.GetFullRouteUseCase
import kotlinx.coroutines.launch

class RouteDetailViewModel(
    private val routeSlug: String,
    private val getFullRoute: GetFullRouteUseCase
) : ViewModel() {

    private val _route = MutableLiveData<GetFullRouteUseCase.FullRoute>()
    val route: LiveData<GetFullRouteUseCase.FullRoute> = _route

    init {
        viewModelScope.launch {
            val route = getFullRoute(routeSlug)
            _route.postValue(route)
        }
    }

}