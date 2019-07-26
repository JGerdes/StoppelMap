package com.jonasgerdes.stoppelmap.map.view

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonasgerdes.stoppelmap.map.usecase.SearchForStallsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapViewModel(
    private val searchForStalls: SearchForStallsUseCase
) : ViewModel() {

    fun onSearchEntered(text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val stalls = searchForStalls(text)
            Log.d("MapViewModel", "Found stalls:")
            stalls.forEach {
                Log.d("MapViewModel", "${it.title}")
            }
        }
    }
}