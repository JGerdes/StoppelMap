package com.jonasgerdes.androidutil.view

import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes


fun TextView.setTextOrHide(text: String?) {
    visibility = if (text == null || text.isEmpty()) View.GONE else View.VISIBLE
    this.text = text
}

fun TextView.setTextOrDefault(text: String?, @StringRes defaultRes: Int) {
    if (text != null) {
        setText(text)
    } else {
        setText(defaultRes)
    }
}