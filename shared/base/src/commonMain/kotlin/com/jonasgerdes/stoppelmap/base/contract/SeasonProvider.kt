package com.jonasgerdes.stoppelmap.base.contract

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

interface SeasonProvider {

    fun getCurrentOrNextSeason(): Season
    fun getThisYearsSeason(): Season
}


interface Season {
    val year: Int
    val iteration: Int
    val days: List<LocalDate>

    val start: LocalDateTime
    val end: LocalDateTime
}
