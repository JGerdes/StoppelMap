package com.jonasgerdes.stoppelmap.shared.dataupdate

import com.jonasgerdes.stoppelmap.base.model.MapDataFile
import com.jonasgerdes.stoppelmap.shared.dataupdate.usecase.UpdateDataUseCase
import com.jonasgerdes.stoppelmap.shared.dataupdate.usecase.UpdateRemoteAppConfigUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DataUpdateDependencies : KoinComponent {
    val updateDataUseCase: UpdateDataUseCase by inject()
    val updateRemoteAppConfigUseCase: UpdateRemoteAppConfigUseCase by inject()
    val mapDataFile: MapDataFile by inject()
}