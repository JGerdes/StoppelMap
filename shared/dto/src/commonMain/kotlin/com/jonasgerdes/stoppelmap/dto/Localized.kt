package com.jonasgerdes.stoppelmap.dto

typealias Locale = String
typealias Localized<T> = Map<Locale, T>

object Locales {
    const val de = "de"
    const val en = "en"
}