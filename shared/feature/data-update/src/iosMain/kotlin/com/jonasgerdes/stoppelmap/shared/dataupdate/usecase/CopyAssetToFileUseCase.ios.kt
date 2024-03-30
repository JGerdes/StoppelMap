package com.jonasgerdes.stoppelmap.shared.dataupdate.usecase

import dev.icerock.moko.resources.AssetResource
import okio.Path

actual class CopyAssetToFileUseCase {
    actual suspend operator fun invoke(
        asset: AssetResource,
        path: Path
    ) {

    }
}