@file:OptIn(ExperimentalForeignApi::class)

package com.jonasgerdes.stoppelmap.shared.dataupdate

import kotlinx.cinterop.ExperimentalForeignApi
import okio.FileSystem
import org.koin.core.scope.Scope

actual fun Scope.bundledDataFileSystem(): FileSystem = TODO()