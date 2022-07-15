package com.jonasgerdes.stoppelmap.countdown.model

sealed class CountDown {
    data class InFuture(
        val daysLeft: Int,
        val hoursLeft: Int,
        val minutesLeft: Int,
    ) : CountDown()

    object OnGoing : CountDown()

    object InPast : CountDown()
}
