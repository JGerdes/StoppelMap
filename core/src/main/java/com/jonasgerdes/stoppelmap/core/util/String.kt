package com.jonasgerdes.stoppelmap.core.util

import android.os.Build
import android.text.Html
import android.text.Html.FROM_HTML_MODE_COMPACT

fun String.fromHtml() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    Html.fromHtml(this, FROM_HTML_MODE_COMPACT)
} else {
    @Suppress("DEPRECATION")
    Html.fromHtml(this)
}