package com.jonasgerdes.stoppelmap.news.ui.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonasgerdes.stoppelmap.news.usecase.GetUnreadNewsCountUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class NewsNavigationIconViewModel(
    getUnreadNewsCount: GetUnreadNewsCountUseCase
) : ViewModel() {

    val state: StateFlow<ViewState> =
        getUnreadNewsCount()
            .map(::ViewState)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = ViewState(),
            )


    data class ViewState(
        val unreadArticles: Long = 0,
    )
}