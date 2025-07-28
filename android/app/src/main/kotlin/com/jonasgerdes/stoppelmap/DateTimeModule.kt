package com.jonasgerdes.stoppelmap

import com.jonasgerdes.stoppelmap.base.contract.ClockProvider
import com.jonasgerdes.stoppelmap.base.contract.SeasonProvider
import com.jonasgerdes.stoppelmap.base.model.AppInfo
import com.jonasgerdes.stoppelmap.base.model.VenueInformation
import com.jonasgerdes.stoppelmap.provider.StoppelmarktClockProvider
import com.jonasgerdes.stoppelmap.provider.StoppelmarktSeasonProvider
import com.jonasgerdes.stoppelmap.settings.data.DateOverride
import com.jonasgerdes.stoppelmap.settings.data.SettingsRepository
import com.jonasgerdes.stoppelmap.util.clockprovider.ClockProviderWrapper
import com.jonasgerdes.stoppelmap.util.clockprovider.ExactDayClockProvider
import com.jonasgerdes.stoppelmap.util.clockprovider.OverrideYearClockProvider
import com.jonasgerdes.stoppelmap.util.clockprovider.TodayInStoMaWeekClockProvider
import kotlinx.datetime.DayOfWeek
import org.koin.dsl.module

val dateTimeModule = module {
    single<SeasonProvider> { StoppelmarktSeasonProvider(clockProvider = get()) }
    single<ClockProvider> {
        val realClockProvider = OverrideYearClockProvider(
            realClockProvider = StoppelmarktClockProvider(
                localTimeZone = get<VenueInformation>().timeZone,
            ),
            year = get<AppInfo>().versionCode.toString().take(2).toInt() + 2000
        )
        val realSeasonProvider = StoppelmarktSeasonProvider(clockProvider = realClockProvider)
        val localTimeZone = get<VenueInformation>().timeZone
        ClockProviderWrapper(
            clockProviderMap = mapOf(
                DateOverride.None to realClockProvider,
                DateOverride.TodayInStoMaWeek to TodayInStoMaWeekClockProvider(
                    seasonProvider = realSeasonProvider,
                    realClockProvider = realClockProvider,
                    localTimeZone = localTimeZone,
                ),
                DateOverride.Wednesday to ExactDayClockProvider(
                    seasonProvider = realSeasonProvider,
                    realClockProvider = realClockProvider,
                    localTimeZone = localTimeZone,
                    DayOfWeek.WEDNESDAY
                ),
                DateOverride.Thursday to ExactDayClockProvider(
                    seasonProvider = realSeasonProvider,
                    realClockProvider = realClockProvider,
                    localTimeZone = localTimeZone,
                    DayOfWeek.THURSDAY
                ),
                DateOverride.Friday to ExactDayClockProvider(
                    seasonProvider = realSeasonProvider,
                    realClockProvider = realClockProvider,
                    localTimeZone = localTimeZone,
                    DayOfWeek.FRIDAY
                ),
                DateOverride.Saturday to ExactDayClockProvider(
                    seasonProvider = realSeasonProvider,
                    realClockProvider = realClockProvider,
                    localTimeZone = localTimeZone,
                    DayOfWeek.SATURDAY
                ),
                DateOverride.Sunday to ExactDayClockProvider(
                    seasonProvider = realSeasonProvider,
                    realClockProvider = realClockProvider,
                    localTimeZone = localTimeZone,
                    DayOfWeek.SUNDAY
                ),
                DateOverride.Monday to ExactDayClockProvider(
                    seasonProvider = realSeasonProvider,
                    realClockProvider = realClockProvider,
                    localTimeZone = localTimeZone,
                    DayOfWeek.MONDAY
                ),
                DateOverride.Tuesday to ExactDayClockProvider(
                    seasonProvider = realSeasonProvider,
                    realClockProvider = realClockProvider,
                    localTimeZone = localTimeZone,
                    DayOfWeek.TUESDAY
                ),
            ),
            settingRepository = get<SettingsRepository>(),
            scope = get(),
        )
    }
}
