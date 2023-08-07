package com.jonasgerdes.stoppelmap.map.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils
import com.jonasgerdes.stoppelmap.theme.settings.MapColorSetting
import com.jonasgerdes.stoppelmap.theme.settings.ThemeSetting

data class MapBoxMapColors(
    val backgroundColor: Color,
    val streetColor: Color,
    val labelColor: Color,
    val labelHaloColor: Color,
    val stallTypeBarColor: Color,
    val stallTypeCandyStallColor: Color,
    val stallTypeExpoColor: Color,
    val stallTypeFoodStallColor: Color,
    val stallTypeGameStallColor: Color,
    val stallTypeMiscColor: Color,
    val stallTypeParkingColor: Color,
    val stallTypeRestaurantColor: Color,
    val stallTypeRestroomColor: Color,
    val stallTypeRideColor: Color,
    val stallTypeSellerStallColor: Color,
) {
    companion object {

        @Composable
        fun fromMaterialTheme(
            isDarkTheme: Boolean,
        ) =
            MapBoxMapColors(
                backgroundColor = MaterialTheme.colorScheme.background,
                streetColor = MaterialTheme.colorScheme.surfaceVariant,
                labelColor = MaterialTheme.colorScheme.onSurface,
                labelHaloColor = MaterialTheme.colorScheme.surface,
                stallTypeBarColor = MaterialTheme.colorScheme.secondaryContainer.modify(isDarkTheme),
                stallTypeCandyStallColor = MaterialTheme.colorScheme.surfaceVariant.modify(
                    isDarkTheme
                ),
                stallTypeExpoColor = MaterialTheme.colorScheme.surfaceVariant.modify(isDarkTheme),
                stallTypeFoodStallColor = MaterialTheme.colorScheme.surfaceVariant.modify(
                    isDarkTheme
                ),
                stallTypeGameStallColor = MaterialTheme.colorScheme.surfaceVariant.modify(
                    isDarkTheme
                ),
                stallTypeMiscColor = MaterialTheme.colorScheme.surfaceVariant.modify(isDarkTheme),
                stallTypeParkingColor = MaterialTheme.colorScheme.surfaceVariant.modify(isDarkTheme),
                stallTypeRestaurantColor = MaterialTheme.colorScheme.surfaceVariant.modify(
                    isDarkTheme
                ),
                stallTypeRestroomColor = MaterialTheme.colorScheme.errorContainer.modify(isDarkTheme),
                stallTypeRideColor = MaterialTheme.colorScheme.tertiaryContainer.modify(isDarkTheme),
                stallTypeSellerStallColor = MaterialTheme.colorScheme.surfaceVariant.modify(
                    isDarkTheme
                ),
            )
    }
}

private fun Color.withHSL(
    hue: Float? = null,
    saturation: Float? = null,
    lightness: Float? = null,
) = FloatArray(3).let { hsl ->
    ColorUtils.colorToHSL(toArgb(), hsl)
    hue?.let { hsl[0] = it }
    saturation?.let { hsl[1] = it }
    lightness?.let { hsl[2] = it }
    Color(ColorUtils.HSLToColor(hsl))
}

private fun Color.modify(isDarkTheme: Boolean) =
    withHSL(lightness = if (isDarkTheme) 0.3f else 0.7f)


@Composable
fun MapTheme.toMapColors(): MapBoxMapColors {
    val isDarkTheme = when (themeSetting) {
        ThemeSetting.Light -> false
        ThemeSetting.Dark -> true
        ThemeSetting.System -> isSystemInDarkTheme()
    }
    return when (mapColorSetting) {
        MapColorSetting.Classic ->
            if (isDarkTheme) MapBoxMapColors(
                backgroundColor = MaterialTheme.colorScheme.background,
                streetColor = Color(0xFF5E5A5C),
                labelColor = Color(0xFFFFFFFF),
                labelHaloColor = Color(0xFF000000),
                stallTypeBarColor = Color(0xFFA4BDF7).withHSL(lightness = 0.3f),
                stallTypeCandyStallColor = Color(0xFFB29CDA).withHSL(lightness = 0.3f),
                stallTypeExpoColor = Color(0xFF283592).withHSL(lightness = 0.3f),
                stallTypeFoodStallColor = Color(0xFFF8EDB2).withHSL(lightness = 0.3f),
                stallTypeGameStallColor = Color(0xFFC7F98C).withHSL(lightness = 0.3f),
                stallTypeMiscColor = Color(0xFFD3AE97).withHSL(lightness = 0.3f),
                stallTypeParkingColor = Color(0xFFDDF9DA).withHSL(lightness = 0.3f),
                stallTypeRestaurantColor = Color(0xFFD3AE97).withHSL(lightness = 0.3f),
                stallTypeRestroomColor = Color(0xFFAC1457).withHSL(lightness = 0.3f),
                stallTypeRideColor = Color(0xFF85D2D8).withHSL(lightness = 0.3f),
                stallTypeSellerStallColor = Color(0xFF8B9DFE).withHSL(lightness = 0.3f),
            )
            else MapBoxMapColors(
                backgroundColor = MaterialTheme.colorScheme.background,
                streetColor = Color(0xFFE4E2E2),
                labelColor = Color(0xFF000000),
                labelHaloColor = Color(0xFFFFFFFF),
                stallTypeBarColor = Color(0xFFA4BDF7),
                stallTypeCandyStallColor = Color(0xFFB29CDA),
                stallTypeExpoColor = Color(0xFF283592),
                stallTypeFoodStallColor = Color(0xFFF8EDB2),
                stallTypeGameStallColor = Color(0xFFC7F98C),
                stallTypeMiscColor = Color(0xFFD3AE97),
                stallTypeParkingColor = Color(0xFFDDF9DA),
                stallTypeRestaurantColor = Color(0xFFD3AE97),
                stallTypeRestroomColor = Color(0xFFAC1457),
                stallTypeRideColor = Color(0xFF85D2D8),
                stallTypeSellerStallColor = Color(0xFF8B9DFE),
            )

        MapColorSetting.AppScheme -> MapBoxMapColors.fromMaterialTheme(isDarkTheme = isDarkTheme)
    }

}
