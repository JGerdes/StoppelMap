package com.jonasgerdes.stoppelmap.util

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.view.View

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 19.04.2018
 */

inline val Int.dp
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

inline fun Context.getColorCompat(@ColorRes color: Int) = ContextCompat.getColor(this, color)

inline fun Activity.toggleLayoutFullscreen(overlap: Boolean) {
    if (overlap) {
        window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    } else {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
    }
}