package com.jonasgerdes.stoppelmap.theme.settings

import android.os.Build
import com.jonasgerdes.stoppelmap.theme.util.supportsDarkTheme

enum class ThemeSetting {
    Light, Dark, System;

    companion object {
        val default = if (Build.VERSION.SDK_INT.supportsDarkTheme()) System else Light
    }
}
