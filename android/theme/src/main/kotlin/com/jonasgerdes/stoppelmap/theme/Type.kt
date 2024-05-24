package com.jonasgerdes.stoppelmap.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

val stoppelMapFontFamily = FontFamily(
    Font(R.font.roboto_slab_thin, FontWeight.Thin),
    Font(R.font.roboto_slab_light, FontWeight.Light),
    Font(R.font.roboto_slab_regular, FontWeight.Normal),
    Font(R.font.roboto_slab_bold, FontWeight.Bold),
)

val Typography = with(Typography()) {
    copy(
        displayLarge = displayLarge.copy(fontFamily = stoppelMapFontFamily),
        displayMedium = displayMedium.copy(fontFamily = stoppelMapFontFamily),
        displaySmall = displaySmall.copy(fontFamily = stoppelMapFontFamily),
        headlineLarge = headlineLarge.copy(fontFamily = stoppelMapFontFamily),
        headlineMedium = headlineMedium.copy(fontFamily = stoppelMapFontFamily),
        headlineSmall = headlineSmall.copy(fontFamily = stoppelMapFontFamily),
        titleLarge = titleLarge.copy(fontFamily = stoppelMapFontFamily),
        titleMedium = titleMedium.copy(fontFamily = stoppelMapFontFamily),
        titleSmall = titleSmall.copy(fontFamily = stoppelMapFontFamily),
        bodyLarge = bodyLarge.copy(fontFamily = stoppelMapFontFamily),
        bodyMedium = bodyMedium.copy(fontFamily = stoppelMapFontFamily),
        bodySmall = bodySmall.copy(fontFamily = stoppelMapFontFamily),
        labelLarge = labelLarge.copy(fontFamily = stoppelMapFontFamily),
        labelMedium = labelMedium.copy(fontFamily = stoppelMapFontFamily),
        labelSmall = labelSmall.copy(fontFamily = stoppelMapFontFamily),
    )
}
