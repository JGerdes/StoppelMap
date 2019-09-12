package com.jonasgerdes.stoppelmap.data

import java.util.*

internal fun <T> List<T>.limit(size: Int) = subList(0, size)

internal fun <T> List<T>.like(query: String, mapper: (T) -> String?) = filter { item ->
    val hasWildcardAtStart = query.startsWith("%")
    val hasWildcardAtEnd = query.endsWith("%")

    val realQuery = query.removePrefix("%").removeSuffix("%").toLowerCase(Locale.GERMAN)
    val value = mapper(item)?.toLowerCase(Locale.GERMAN)

    when {
        value == null -> false
        hasWildcardAtStart && hasWildcardAtEnd -> value.contains(realQuery)
        hasWildcardAtStart -> value.endsWith(realQuery)
        hasWildcardAtEnd -> value.startsWith(realQuery)
        else -> value == realQuery
    }
}