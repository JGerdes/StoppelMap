package com.jonasgerdes.stoppelmap.util

import android.graphics.PorterDuff
import android.support.v4.content.ContextCompat
import android.widget.ImageView

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 13.06.17
 */


fun ImageView.setTint(colorResource: Int) {
    val color = ContextCompat.getColor(context, colorResource)
    setColorFilter(color, PorterDuff.Mode.SRC_IN)
}
