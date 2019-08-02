package com.jonasgerdes.androidutil

import android.graphics.Rect

fun Rect.insetBy(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) =
    Rect(this.left + left, this.top + top, this.right - right, this.bottom - bottom)