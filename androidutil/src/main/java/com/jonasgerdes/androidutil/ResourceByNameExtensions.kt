package com.jonasgerdes.androidutil

import android.content.Context
import androidx.core.content.ContextCompat

fun Context.getDrawableByName(resName: String, default: Int = 0) =
    resources.getIdentifier(resName, "drawable", packageName).let {
        if (it == 0) default else it
    }

fun Context.getColorByName(resName: String, default: Int = 0) =
    ContextCompat.getColor(this, resources.getIdentifier(resName, "color", packageName).let {
        if (it == 0) default else it
    })
