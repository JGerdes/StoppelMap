package com.jonasgerdes.stoppelmap.data.shared


typealias Locale = String
typealias Localized<T> = Map<Locale, T>

object Locales {
    const val de = "de"
    const val en = "en"
}

fun Localized_stringQueries.getLocalesForKeys(keys: Collection<String>): Map<String, Localized<String>> =
    getByKeys(keys).executeAsList()
        .groupBy { it.key }
        .mapValues {
            it.value.groupBy(keySelector = { it.locale }, valueTransform = { it.string })
                .mapValues { it.value.first() }
        }
