package com.jonasgerdes.stoppelmap.domain

import com.jonasgerdes.stoppelmap.core.domain.GlobalInfoProvider
import org.threeten.bp.*

class StoppelmarktInfoProvider : GlobalInfoProvider {

    override fun getAreaBounds(): GlobalInfoProvider.AreaBounds = GlobalInfoProvider.AreaBounds(
        northLatitude = 52.7494011815008,
        southLatitude = 52.7429499584193,
        westLongitude = 8.28653654801576,
        eastLongitude = 8.30059127365977
    )

    override fun getCurrentSeason(): GlobalInfoProvider.Season {
        return GlobalInfoProvider.Season(
            title = "Stoppelmarkt 2019",
            start = LocalDateTime.of(
                2019,
                Month.AUGUST,
                15,
                18,
                0,
                0
            ).atOffset(ZoneOffset.ofHours(2)),
            days = calculateDatesForYear(2019)
        )
    }

    override fun getSeasons() = listOf(getCurrentSeason()).union(
        (-1..3).map { offset ->
            val year = LocalDate.now().year + offset
            val days = calculateDatesForYear(year)
            GlobalInfoProvider.Season(
                title = "Stoppelmarkt $year",
                start = LocalDateTime.of(days.first(), LocalTime.of(18, 0, 0))
                    .atOffset(ZoneOffset.ofHours(2)),
                days = days
            )
        }).sortedBy { it.start }
}