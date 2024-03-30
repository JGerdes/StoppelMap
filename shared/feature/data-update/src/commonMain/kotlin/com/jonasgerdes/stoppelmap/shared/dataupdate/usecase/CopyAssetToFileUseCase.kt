package com.jonasgerdes.stoppelmap.shared.dataupdate.usecase

import dev.icerock.moko.resources.AssetResource
import okio.Path

expect class CopyAssetToFileUseCase {
    suspend operator fun invoke(asset: AssetResource, path: Path)
}