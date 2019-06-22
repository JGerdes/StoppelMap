package com.jonasgerdes.stoppelmap.home.view

import org.threeten.bp.Duration


sealed class HomeCard

object MoreCardsInfoCard : HomeCard()
data class CountdownCard(val duration: Duration) : HomeCard()