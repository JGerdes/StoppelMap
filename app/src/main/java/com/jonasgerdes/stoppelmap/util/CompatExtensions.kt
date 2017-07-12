package com.jonasgerdes.stoppelmap.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 12.06.17
 */

fun Context.getDrawableCompat(id: Int): Drawable {
    return ContextCompat.getDrawable(this, id)
}