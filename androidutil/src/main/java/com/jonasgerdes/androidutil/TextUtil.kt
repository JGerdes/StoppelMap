package com.jonasgerdes.androidutil

import android.os.Build
import android.text.Html

fun htmlToSpanned(html: String) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    Html.fromHtml(html, 0)
} else {
    Html.fromHtml(html)
}