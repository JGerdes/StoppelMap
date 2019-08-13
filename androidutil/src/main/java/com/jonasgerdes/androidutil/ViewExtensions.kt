package com.jonasgerdes.androidutil

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes

fun ViewGroup.inflate(@LayoutRes layout: Int) =
    LayoutInflater.from(context).inflate(layout, this, false)