package com.jonasgerdes.stoppelmap.data.conversion.database.extensions

import com.jonasgerdes.stoppelmap.data.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.data.shared.Localized_string
import com.jonasgerdes.stoppelmap.dto.Localized

internal fun StoppelMapDatabase.addLocalizedString(
    localizedString: Localized<String>,
    vararg keyParts: String,
): String {
    val key = keyParts.joinToString(separator = "_")
    localizedString.entries.forEach {
        localized_stringQueries.insert(
            Localized_string(
                key = key,
                locale = it.key,
                string = it.value,
            )
        )
    }
    return key
}