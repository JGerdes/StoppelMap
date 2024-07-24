package com.jonasgerdes.stoppelmap.data.conversion.database.extensions

import com.jonasgerdes.stoppelmap.data.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.data.shared.Alias
import com.jonasgerdes.stoppelmap.dto.Localized

internal fun StoppelMapDatabase.addAliases(
    referenceSlug: String,
    aliases: List<Localized<String>>?
) {
    aliases?.forEachIndexed { index, alias ->
        aliasQueries.insert(
            Alias(
                referenceSlug = referenceSlug,
                aliasKey = addLocalizedString(
                    alias,
                    referenceSlug,
                    "alias",
                    index.toString().padStart(2, '0')
                )
            )
        )
    }
}