package com.jonasgerdes.stoppelmap.shared.dataupdate.usecase

import co.touchlab.kermit.Logger
import dev.icerock.moko.resources.AssetResource
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import okio.Path
import platform.Foundation.NSError
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
actual class CopyAssetToFileUseCase {
    actual suspend operator fun invoke(
        asset: AssetResource,
        path: Path
    ) {
        Logger.d { "Copy ${asset.path} to ${path.toString()}" }
        memScoped {
            val errorPtr: ObjCObjectVar<NSError?> = alloc<ObjCObjectVar<NSError?>>()
            NSFileManager.defaultManager.copyItemAtURL(
                srcURL = asset.url,
                toURL = NSURL.URLWithString(URLString = "file://$path")!!,
                error = errorPtr.ptr
            )
            val error = errorPtr.value
            if (error != null) {
                Logger.w { "Error copying ${asset.path} to ${path}: ${error.localizedDescription}" }
            } else {
                Logger.d { "Successfully copied" }
            }
        }
    }
}