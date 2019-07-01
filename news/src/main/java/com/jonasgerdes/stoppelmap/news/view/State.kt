package com.jonasgerdes.stoppelmap.news.view

sealed class LoadingState {
    object Idle : LoadingState()
    sealed class Loading : LoadingState() {
        object Refresh : Loading()
        object More : Loading()
    }

    sealed class Error : LoadingState() {
        object Unknown : Error()
        object NoNetwork : Error()
    }
}