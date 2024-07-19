package com.jonasgerdes.stoppelmap.server.callextensions

import com.jonasgerdes.stoppelmap.server.statusexceptions.AuthorizationException
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.header

fun ApplicationCall.assertHasApiKey(apiKey: String) {
    if (request.header("X-API-Key") != apiKey) {
        throw AuthorizationException(message = "Missing API key")
    }
}