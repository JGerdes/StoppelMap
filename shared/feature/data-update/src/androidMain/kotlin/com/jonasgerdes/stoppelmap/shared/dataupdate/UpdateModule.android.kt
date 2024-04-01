package com.jonasgerdes.stoppelmap.shared.dataupdate

import android.content.Context
import com.jonasgerdes.stoppelmap.shared.dataupdate.usecase.CopyAssetToFileUseCase
import com.jonasgerdes.stoppelmap.shared.dataupdate.usecase.RemoveDatabaseFileUseCase
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import okio.Path
import okio.Path.Companion.toPath
import org.koin.core.scope.Scope

actual fun Scope.createHttpClientEngine(): HttpClientEngine {
    return OkHttp.create()
}

actual fun Scope.createTempPath(name: String): Path {
    return get<Context>().cacheDir.resolve(name).absolutePath.toPath()
}

actual fun Scope.createRemoveDatabaseUseCase(): RemoveDatabaseFileUseCase {
    return RemoveDatabaseFileUseCase(get<Context>())
}

actual fun Scope.createCopyAssetToFileUseCase(): CopyAssetToFileUseCase {
    return CopyAssetToFileUseCase(get<Context>())
}