package com.jonasgerdes.stoppelmap.shared.network

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.core.scope.Scope

actual fun Scope.createHttpClientEngine(): HttpClientEngine {
    return Darwin.create()
}
