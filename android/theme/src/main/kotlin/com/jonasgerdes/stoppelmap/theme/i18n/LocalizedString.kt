package com.jonasgerdes.stoppelmap.theme.i18n

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.core.os.ConfigurationCompat


@Composable
fun localizedString(localizedString: Map<String, String>): String {
    val locales = ConfigurationCompat.getLocales(LocalConfiguration.current)
    val locale = locales.getFirstMatch(localizedString.keys.toTypedArray())

    return localizedString[locale ?: "de"] ?: localizedString.entries.first().value
}
