package com.jonasgerdes.stoppelmap.data.conversion.database.extensions

import com.jonasgerdes.stoppelmap.data.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.data.shared.Alias
import com.jonasgerdes.stoppelmap.dto.data.Alias as DtoAlias

internal fun StoppelMapDatabase.addAliases(
    referenceSlug: String,
    aliases: List<DtoAlias>?
) {
    aliases?.forEachIndexed { index, alias ->
        aliasQueries.insert(
            Alias(
                referenceSlug = referenceSlug,
                string = alias.string,
                locale = alias.locale,
            )
        )
    }
}