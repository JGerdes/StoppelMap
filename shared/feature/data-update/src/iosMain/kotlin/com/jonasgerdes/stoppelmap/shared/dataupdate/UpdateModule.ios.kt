@file:OptIn(ExperimentalForeignApi::class)

package com.jonasgerdes.stoppelmap.shared.dataupdate

import com.jonasgerdes.stoppelmap.shared.dataupdate.usecase.CopyAssetToFileUseCase
import com.jonasgerdes.stoppelmap.shared.dataupdate.usecase.RemoveDatabaseFileUseCase
import kotlinx.cinterop.ExperimentalForeignApi
import okio.Path
import okio.Path.Companion.toPath
import org.koin.core.scope.Scope
import platform.Foundation.NSData
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
actual fun Scope.createTempPath(name: String): Path {
    val dir = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    NSFileManager.defaultManager.createFileAtPath(dir!!.path + "/$name", NSData.new(), null)
    return (dir.path + "/$name").toPath()
}

actual fun Scope.createRemoveDatabaseUseCase(): RemoveDatabaseFileUseCase {
    return RemoveDatabaseFileUseCase()
}

actual fun Scope.createCopyAssetToFileUseCase(): CopyAssetToFileUseCase {
    return CopyAssetToFileUseCase()
}