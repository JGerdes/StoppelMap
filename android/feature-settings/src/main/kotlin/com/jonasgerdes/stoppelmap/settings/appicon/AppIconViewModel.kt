package com.jonasgerdes.stoppelmap.settings.appicon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber

class AppIconViewModel(
    private val appIconRepository: AppIconRepository,
) : ViewModel() {

    val state: StateFlow<ViewState> =
        appIconRepository.appIcons
            .map { icons ->
                Timber.d("Icons: $icons")
                ViewState.Loaded(
                    appIcons = icons,
                )
            }
            .onStart {
                appIconRepository.updateAppIcons()
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = ViewState.Loading
            )


    fun onAppIconSelect(icon: AppIcon) {
        appIconRepository.setIcon(icon)
    }

    sealed interface ViewState {
        data object Loading : ViewState
        data class Loaded(val appIcons: List<AppIcon>) : ViewState
    }
}
