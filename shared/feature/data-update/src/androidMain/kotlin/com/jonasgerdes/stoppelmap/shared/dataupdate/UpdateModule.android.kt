package com.jonasgerdes.stoppelmap.shared.dataupdate

import android.content.Context
import dev.icerock.moko.resources.AssetResource
import okio.Path
import okio.Path.Companion.toPath
import okio.assetfilesystem.asFileSystem
import org.koin.core.scope.Scope


actual fun Scope.bundledDataFileSystem() = get<Context>().assets.asFileSystem()
actual fun Scope.getPathForAssetResource(assetResource: AssetResource): Path = assetResource.originalPath.toPath()