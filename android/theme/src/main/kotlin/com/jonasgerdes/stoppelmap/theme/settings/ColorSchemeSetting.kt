package com.jonasgerdes.stoppelmap.theme.settings

import android.os.Build
import com.jonasgerdes.stoppelmap.theme.util.supportsDynamicColor

enum class ColorSchemeSetting {
    Classic, System;

    companion object {
        val default = if (Build.VERSION.SDK_INT.supportsDynamicColor()) System else Classic
    }
}
