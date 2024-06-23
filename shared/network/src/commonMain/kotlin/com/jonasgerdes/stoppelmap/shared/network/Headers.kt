package com.jonasgerdes.stoppelmap.shared.network

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header

fun HttpRequestBuilder.apiKeyHeader(apiKey: String) = header("X-API-Key", apiKey)