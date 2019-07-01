package com.jonasgerdes.androidutil.view

import android.view.View
import androidx.core.view.updatePadding

fun View.consumeWindowInsetsTop() {
    setOnApplyWindowInsetsListener { v, insets ->
        insets.apply { updatePadding(top = insets.systemWindowInsetTop) }
    }
    requestApplyInsets()
}