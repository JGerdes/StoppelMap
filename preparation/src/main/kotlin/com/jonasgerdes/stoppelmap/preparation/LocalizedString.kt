package com.jonasgerdes.stoppelmap.preparation

import com.jonasgerdes.stoppelmap.dto.Locales

fun localizedString(
    de: String,
    en: String,
) = mapOf(
    Locales.de to de,
    Locales.en to en,
)

fun localizedString(
    valueForAllLocales: String,
) = com.jonasgerdes.stoppelmap.preparation.localizedString(
    de = valueForAllLocales,
    en = valueForAllLocales,
)
