package com.jonasgerdes.stoppelmap.shared.dataupdate

import com.jonasgerdes.stoppelmap.shared.dataupdate.usecase.CopyAssetToFileUseCase
import com.jonasgerdes.stoppelmap.shared.dataupdate.usecase.RemoveDatabaseFileUseCase
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import kotlinx.cinterop.ExperimentalForeignApi
import okio.Path
import okio.Path.Companion.toPath
import org.koin.core.scope.Scope
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
    return (requireNotNull(documentDirectory).path + "/$name").toPath()
}

actual fun Scope.createTempPath(name: String): Path {
    TODO("Not yet implemented")
}

actual fun Scope.createRemoveDatabaseUseCase(): RemoveDatabaseFileUseCase {
    TODO("Not yet implemented")
}

actual fun Scope.createCopyAssetToFileUseCase(): CopyAssetToFileUseCase {
    TODO("Not yet implemented")
}