package com.jonasgerdes.stoppelmap.countdown.model

sealed class CountDownState {
    object Loading : CountDownState()
    data class CountingDown(
        val daysLeft: Int,
        val hoursLeft: Int,
        val minutesLeft: Int,
        val year: Int,
        val showCurrentSeasonIsOverHint: Boolean
    ) : CountDownState()

    object Over : CountDownState()
}