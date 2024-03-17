package com.jonasgerdes.stoppelmap

import com.jonasgerdes.stoppelmap.base.contract.ClockProvider
import com.jonasgerdes.stoppelmap.base.contract.SeasonProvider
import com.jonasgerdes.stoppelmap.provider.StoppelmarktClockProvider
import com.jonasgerdes.stoppelmap.provider.StoppelmarktSeasonProvider
import com.jonasgerdes.stoppelmap.settings.data.DateOverride
import com.jonasgerdes.stoppelmap.settings.data.SettingsRepository
import com.jonasgerdes.stoppelmap.util.clockprovider.ClockProviderWrapper
import com.jonasgerdes.stoppelmap.util.clockprovider.ExactDayClockProvider
import com.jonasgerdes.stoppelmap.util.clockprovider.TodayInStoMaWeekClockProvider
import kotlinx.datetime.DayOfWeek
import org.koin.dsl.module

val dateTimeModule = module {
    single<SeasonProvider> { StoppelmarktSeasonProvider(clockProvider = get()) }
    single<ClockProvider> {
        val realClockProvider = StoppelmarktClockProvider()
        val realSeasonProvider = StoppelmarktSeasonProvider(clockProvider = realClockProvider)
        ClockProviderWrapper(
            clockProviderMap = mapOf(
                DateOverride.None to realClockProvider,
                DateOverride.TodayInStoMaWeek to TodayInStoMaWeekClockProvider(
                    seasonProvider = realSeasonProvider,
                    realClockProvider = realClockProvider
                ),
                DateOverride.Wednesday to ExactDayClockProvider(
                    seasonProvider = realSeasonProvider,
                    realClockProvider = realClockProvider,
                    DayOfWeek.WEDNESDAY
                ),
                DateOverride.Thursday to ExactDayClockProvider(
                    seasonProvider = realSeasonProvider,
                    realClockProvider = realClockProvider,
                    DayOfWeek.THURSDAY
                ),
                DateOverride.Friday to ExactDayClockProvider(
                    seasonProvider = realSeasonProvider,
                    realClockProvider = realClockProvider,
                    DayOfWeek.FRIDAY
                ),
                DateOverride.Saturday to ExactDayClockProvider(
                    seasonProvider = realSeasonProvider,
                    realClockProvider = realClockProvider,
                    DayOfWeek.SATURDAY
                ),
                DateOverride.Sunday to ExactDayClockProvider(
                    seasonProvider = realSeasonProvider,
                    realClockProvider = realClockProvider,
                    DayOfWeek.SUNDAY
                ),
                DateOverride.Monday to ExactDayClockProvider(
                    seasonProvider = realSeasonProvider,
                    realClockProvider = realClockProvider,
                    DayOfWeek.MONDAY
                ),
                DateOverride.Tuesday to ExactDayClockProvider(
                    seasonProvider = realSeasonProvider,
                    realClockProvider = realClockProvider,
                    DayOfWeek.TUESDAY
                ),
            ),
            settingRepository = get<SettingsRepository>(),
            scope = get(),
        )
    }
}
