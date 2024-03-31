@file:OptIn(ExperimentalForeignApi::class)

package com.jonasgerdes.stoppelmap.shared.dataupdate

import co.touchlab.kermit.Logger
import com.jonasgerdes.stoppelmap.shared.dataupdate.usecase.CopyAssetToFileUseCase
import com.jonasgerdes.stoppelmap.shared.dataupdate.usecase.RemoveDatabaseFileUseCase
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import kotlinx.cinterop.ExperimentalForeignApi
import okio.Path
import okio.Path.Companion.toPath
import org.koin.core.scope.Scope
import platform.Foundation.NSData
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

actual fun Scope.createHttpClientEngine(): HttpClientEngine {
    return Darwin.create()
}

@OptIn(ExperimentalForeignApi::class)
actual fun Scope.createDataStorePath(name: String): Path {
    val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return (requireNotNull(documentDirectory).path + "/$name").toPath().also {
        Logger.d { "CreateDataStorePath: $it" }
    }
}

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