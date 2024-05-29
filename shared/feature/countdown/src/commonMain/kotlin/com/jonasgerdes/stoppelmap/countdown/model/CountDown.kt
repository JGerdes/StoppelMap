package com.jonasgerdes.stoppelmap.countdown.model

import com.jonasgerdes.stoppelmap.base.contract.Season

sealed interface CountDown {
    val season: Season

    data class InFuture(
        val daysLeft: Int,
        val hoursLeft: Int,
        val minutesLeft: Int,
        val secondsLeft: Int,
        override val season: Season,
    ) : CountDown

    data class OnGoing(
        override val season: Season
    ) : CountDown
}
