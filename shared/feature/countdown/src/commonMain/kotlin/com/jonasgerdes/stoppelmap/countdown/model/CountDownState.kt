package com.jonasgerdes.stoppelmap.countdown.model

import com.jonasgerdes.stoppelmap.base.contract.Season

sealed class CountDownState {
    object Loading : CountDownState()
    data class CountingDown(
        val daysLeft: Int,
        val hoursLeft: Int,
        val minutesLeft: Int,
        val secondsLeft: Int,
        val season: Season,
        val showCurrentSeasonIsOverHint: Boolean
    ) : CountDownState()

    object Over : CountDownState()
}