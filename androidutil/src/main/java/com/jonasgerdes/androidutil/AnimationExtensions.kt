package com.jonasgerdes.androidutil

import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable

fun Drawable.startIfAnimatable() = when (this) {
    is AnimatedVectorDrawable -> start()
    else -> {
        //do nothing
    }
}