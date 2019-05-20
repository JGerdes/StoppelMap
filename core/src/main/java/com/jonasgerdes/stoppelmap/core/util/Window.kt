package com.jonasgerdes.stoppelmap.core.util

import android.os.Build
import android.view.Window

fun Window.enableTransparentStatusBar() {
    decorView.systemUiVisibility = if (Build.VERSION.SDK_INT >= 23)
        android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    else
        android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
}