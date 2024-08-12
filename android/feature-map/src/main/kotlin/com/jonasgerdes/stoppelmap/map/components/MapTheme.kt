package com.jonasgerdes.stoppelmap.map.components

import com.jonasgerdes.stoppelmap.theme.settings.MapColorSetting
import com.jonasgerdes.stoppelmap.theme.settings.ThemeSetting

data class MapTheme(
    val mapColorSetting: MapColorSetting = MapColorSetting.default,
    val themeSetting: ThemeSetting = ThemeSetting.default,
)