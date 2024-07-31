package com.jonasgerdes.stoppelmap.shared.dataupdate

import android.content.Context
import okio.assetfilesystem.asFileSystem
import org.koin.core.scope.Scope


actual fun Scope.bundledDataFileSystem() = get<Context>().assets.asFileSystem()