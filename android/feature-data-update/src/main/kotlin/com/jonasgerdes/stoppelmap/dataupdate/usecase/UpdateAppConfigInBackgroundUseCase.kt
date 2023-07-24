package com.jonasgerdes.stoppelmap.dataupdate.usecase

import com.jonasgerdes.stoppelmap.dataupdate.AppConfigRepository

class UpdateAppConfigInBackgroundUseCase(
    private val appConfigRepository: AppConfigRepository,
) {

    operator fun invoke() = appConfigRepository.updateInBackground()
}
