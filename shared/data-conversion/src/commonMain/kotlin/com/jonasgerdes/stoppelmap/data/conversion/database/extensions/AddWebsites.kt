package com.jonasgerdes.stoppelmap.data.conversion.database.extensions

import com.jonasgerdes.stoppelmap.data.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.data.shared.Website

internal fun StoppelMapDatabase.addWebsites(referenceSlug: String, websites: List<String>) {
    websites.forEach {
        websiteQueries.insert(
            Website(
                referenceSlug = referenceSlug,
                url = it
            )
        )
    }
}