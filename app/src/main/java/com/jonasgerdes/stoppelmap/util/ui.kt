package com.jonasgerdes.stoppelmap.util

import android.content.Context
import android.content.res.Resources
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 19.04.2018
 */

inline val Int.dp
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

inline fun Context.getColorCompat(@ColorRes color: Int) = ContextCompat.getColor(this, color)
