package com.jonasgerdes.stoppelmap.shared.dataupdate.usecase

import android.content.Context
import dev.icerock.moko.resources.AssetResource
import okio.Path

actual class CopyAssetToFileUseCase(private val context: Context) {
    actual suspend operator fun invoke(
        asset: AssetResource,
        path: Path
    ) {
        asset.getInputStream(context).copyTo(path.toFile().outputStream())
    }
}