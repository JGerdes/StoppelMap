package com.jonasgerdes.androidutil

import android.content.res.Resources

inline val Int.dp
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

inline val Double.dp
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()