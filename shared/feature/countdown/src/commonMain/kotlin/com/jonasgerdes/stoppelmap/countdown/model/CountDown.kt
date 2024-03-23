package com.jonasgerdes.stoppelmap.countdown.model

sealed interface CountDown {
    val year: Int

    data class InFuture(
        val daysLeft: Int,
        val hoursLeft: Int,
        val minutesLeft: Int,
        val secondsLeft: Int,
        override val year: Int,
    ) : CountDown

    data class OnGoing(
        override val year: Int
    ) : CountDown
}
