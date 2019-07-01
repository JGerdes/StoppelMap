package com.jonasgerdes.stoppelmap.core.util

import android.content.res.Resources

val Int.dp get() = (this * Resources.getSystem().displayMetrics.density).toInt()
val Float.dp get() = (this * Resources.getSystem().displayMetrics.density)