package com.jonasgerdes.stoppelmap.countdown.ui

import androidx.annotation.FontRes
import androidx.annotation.StringRes
import com.jonasgerdes.stoppelmap.countdown.R

enum class Font(
    val originalAsset: String,
    @FontRes val fontResource: Int,
    @StringRes val fontName: Int,
) {
    // Legacy wrapper - in old versions font is load from assets
    // and file name was saved in shared prefs.
    Roboto("Roboto-Thin.ttf", R.font.roboto_thin, R.string.widget_configuration_font_name_roboto),
    RobotoSlab(
        "RobotoSlab-Light.ttf",
        R.font.roboto_slab_light,
        R.string.widget_configuration_font_name_roboto_slab
    ),
    Damion("Damion-Regular.ttf", R.font.damion, R.string.widget_configuration_font_name_damion);
}
