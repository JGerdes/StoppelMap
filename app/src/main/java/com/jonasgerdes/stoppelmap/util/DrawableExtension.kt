package com.jonasgerdes.stoppelmap.util

import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build

fun Drawable.getCornerRadiusCompat() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
        && this is GradientDrawable) {
    cornerRadius.toInt()
} else {
    -1
}