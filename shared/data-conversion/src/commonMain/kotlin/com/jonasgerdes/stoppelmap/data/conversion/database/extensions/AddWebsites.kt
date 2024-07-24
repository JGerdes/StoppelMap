package com.jonasgerdes.stoppelmap.data.conversion.database.extensions

import com.jonasgerdes.stoppelmap.data.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.data.shared.Website
import com.jonasgerdes.stoppelmap.dto.data.Website as DtoWebsite

internal fun StoppelMapDatabase.addWebsites(referenceSlug: String, websites: List<DtoWebsite>) {
    websites.forEachIndexed { index, website ->
        websiteQueries.insert(
            Website(
                referenceSlug = referenceSlug,
                url = website.url,
                labelKey = website.label?.let {
                    addLocalizedString(
                        it,
                        referenceSlug,
                        "website",
                        index.toString().padStart(2, '0'),
                        "label"
                    )
                }
            )
        )
    }
}