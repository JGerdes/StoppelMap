package com.jonasgerdes.stoppelmap.preparation

import com.jonasgerdes.stoppelmap.dto.Locale

fun localizedString(
    de: String,
    en: String,
) = mapOf(
    Locale.de to de,
    Locale.en to en,
)

fun localizedString(
    valueForAllLocales: String,
) = com.jonasgerdes.stoppelmap.preparation.localizedString(
    de = valueForAllLocales,
    en = valueForAllLocales,
)
