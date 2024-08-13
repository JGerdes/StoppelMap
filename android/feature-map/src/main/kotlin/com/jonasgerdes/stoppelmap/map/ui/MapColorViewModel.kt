package com.jonasgerdes.stoppelmap.map.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonasgerdes.stoppelmap.map.components.MapTheme
import com.jonasgerdes.stoppelmap.settings.data.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MapColorViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val mapColorState = settingsRepository.getSettings()
        .map {
            MapTheme(
                mapColorSetting = it.mapColorSetting,
                themeSetting = it.themeSetting
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = MapTheme()
        )
}