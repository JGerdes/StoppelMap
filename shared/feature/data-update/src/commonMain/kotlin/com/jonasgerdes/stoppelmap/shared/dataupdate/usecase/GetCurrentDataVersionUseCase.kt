package com.jonasgerdes.stoppelmap.shared.dataupdate.usecase

import com.jonasgerdes.stoppelmap.shared.dataupdate.repository.DataUpdateRepository

class GetCurrentDataVersionUseCase(
    private val dataUpdateRepository: DataUpdateRepository,
) {
    operator fun invoke() = dataUpdateRepository.currentDataVersion
}