package com.jonasgerdes.stoppelmap.di

import com.jonasgerdes.stoppelmap.data.shared.Localized
import platform.Foundation.NSLocale
import platform.Foundation.preferredLanguages

fun Localized<String>.localized(): String {
    val language = NSLocale.preferredLanguages().firstOrNull { keys.contains(it) } as String?
    return getOrElse(language ?: "de") { values.first() }
}