package com.jonasgerdes.stoppelmap.util

import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.ViewGroup

fun ViewGroup.inflate(@LayoutRes layout: Int) =
        LayoutInflater.from(context).inflate(layout, this, false)