package com.jonasgerdes.stoppelmap.settings.usecase

import com.jonasgerdes.stoppelmap.settings.data.SettingsRepository

class GetSettingsUseCase(
    private val settingsRepository: SettingsRepository,
) {
    operator fun invoke() = settingsRepository.getSettings()
}
