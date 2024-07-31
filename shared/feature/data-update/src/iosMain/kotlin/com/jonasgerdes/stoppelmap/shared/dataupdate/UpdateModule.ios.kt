@file:OptIn(ExperimentalForeignApi::class)

package com.jonasgerdes.stoppelmap.shared.dataupdate

import dev.icerock.moko.resources.AssetResource
import kotlinx.cinterop.ExperimentalForeignApi
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import org.koin.core.scope.Scope

actual fun Scope.bundledDataFileSystem(): FileSystem = FileSystem.SYSTEM
actual fun Scope.getPathForAssetResource(assetResource: AssetResource): Path =
    assetResource.url.path!!.toPath()