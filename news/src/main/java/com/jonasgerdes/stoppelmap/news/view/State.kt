package com.jonasgerdes.stoppelmap.news.view

sealed class LoadingState {
    object Idle : LoadingState()
    object Loading : LoadingState()
    sealed class Error : LoadingState() {
        object Unknown : Error()
        object NoNetwork : Error()
    }
}