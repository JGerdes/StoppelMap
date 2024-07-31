package com.jonasgerdes.stoppelmap.shared.dataupdate.usecase

import com.jonasgerdes.stoppelmap.shared.dataupdate.repository.AppConfigRepository

class UpdateRemoteAppConfigUseCase(
    private val appConfigRepository: AppConfigRepository
) {
    suspend operator fun invoke() {
        appConfigRepository.updateAppConfig()
    }
}