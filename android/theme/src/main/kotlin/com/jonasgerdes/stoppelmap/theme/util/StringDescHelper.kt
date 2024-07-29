package com.jonasgerdes.stoppelmap.theme.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import dev.icerock.moko.resources.desc.StringDesc

@Composable
@ReadOnlyComposable
fun stringDesc(stringDesc: StringDesc): String {
    LocalConfiguration.current
    return stringDesc.toString(LocalContext.current)
}

@Composable
@ReadOnlyComposable
fun localizedString(localizedString: Map<String, String>): String {
    LocalConfiguration.current
    val locale =
        LocalConfiguration.current.locales.getFirstMatch(localizedString.keys.toTypedArray())
    return localizedString.getOrDefault(locale ?: "de", localizedString.values.first())
}