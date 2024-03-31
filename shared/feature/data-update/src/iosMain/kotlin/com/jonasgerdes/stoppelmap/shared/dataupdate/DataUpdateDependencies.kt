package com.jonasgerdes.stoppelmap.shared.dataupdate

import com.jonasgerdes.stoppelmap.base.model.MapDataFile
import com.jonasgerdes.stoppelmap.shared.dataupdate.usecase.CopyAssetDataFilesUseCase
import com.jonasgerdes.stoppelmap.shared.dataupdate.usecase.UpdateAppConfigAndDownloadFilesUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DataUpdateDependencies : KoinComponent {
    val copyAssetDataFilesUseCase: CopyAssetDataFilesUseCase by inject()
    val updateAppConfigAndDownloadFilesUseCase: UpdateAppConfigAndDownloadFilesUseCase by inject()
    val mapDataFile: MapDataFile by inject()
}