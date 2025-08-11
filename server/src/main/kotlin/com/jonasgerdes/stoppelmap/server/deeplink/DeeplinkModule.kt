package com.jonasgerdes.stoppelmap.server.deeplink

import com.jonasgerdes.stoppelmap.server.deeplink.data.DeeplinkRepository
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val deeplinkModule = module {
    single {
        DeeplinkRepository(
            serverConfig = get(),
            json = Json {
                ignoreUnknownKeys = true
                isLenient = true
            },
            clockProvider = get()
        )
    }

    single {
        DeeplinkController(
            deeplinkRepository = get(),
            serverConfig = get(),
        )
    }
}