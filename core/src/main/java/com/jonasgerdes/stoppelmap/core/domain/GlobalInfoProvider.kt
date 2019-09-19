package com.jonasgerdes.stoppelmap.core.domain

import org.threeten.bp.LocalDate
import org.threeten.bp.OffsetDateTime

interface GlobalInfoProvider {

    fun getAreaBounds(): AreaBounds

    data class AreaBounds(
        val northLatitude: Double,
        val southLatitude: Double,
        val eastLongitude: Double,
        val westLongitude: Double
    )

    data class Season(
        val title: String,
        val start: OffsetDateTime,
        val days: List<LocalDate>
    ) {
        override fun equals(other: Any?): Boolean {
            return if (other !is Season) false
            else other.days == this.days
        }

        override fun hashCode() = days.hashCode()
    }

    fun getCurrentSeason(): Season
    fun getSeasons(): List<Season>
}
