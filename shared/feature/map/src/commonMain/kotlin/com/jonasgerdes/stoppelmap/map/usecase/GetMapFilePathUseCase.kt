package com.jonasgerdes.stoppelmap.map.usecase

import co.touchlab.kermit.Logger
import com.jonasgerdes.stoppelmap.shared.dataupdate.repository.DataUpdateRepository
import kotlinx.coroutines.flow.onEach

class GetMapFilePathUseCase(
    private val appUpdateRepository: DataUpdateRepository,
) {

    operator fun invoke() = appUpdateRepository.mapFile.onEach { Logger.d { "MapFile updated to $it" } }
}