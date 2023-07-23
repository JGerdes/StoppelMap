package com.jonasgerdes.stoppelmap.util.clockprovider

import com.jonasgerdes.stoppelmap.base.contract.ClockProvider
import com.jonasgerdes.stoppelmap.settings.data.DateOverride
import com.jonasgerdes.stoppelmap.settings.data.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime

class ClockProviderWrapper(
    private val clockProviderMap: Map<DateOverride, ClockProvider>,
    settingRepository: SettingsRepository,
    scope: CoroutineScope,
) : ClockProvider {

    private var currentProvider: ClockProvider = default

    init {
        settingRepository.getSettings()
            .onEach {
                currentProvider =
                    clockProviderMap.getOrDefault(it.dateOverride, default)
            }
            .launchIn(scope)
    }

    override fun nowAsInstant(): Instant = currentProvider.nowAsInstant()

    override fun nowAsLocalDateTime(): LocalDateTime = currentProvider.nowAsLocalDateTime()

    override fun toLocalDateTime(instant: Instant): LocalDateTime =
        currentProvider.toLocalDateTime(instant)

    override fun toInstant(localDateTime: LocalDateTime): Instant =
        currentProvider.toInstant(localDateTime)

    private val default get() = clockProviderMap.entries.first().value

}
