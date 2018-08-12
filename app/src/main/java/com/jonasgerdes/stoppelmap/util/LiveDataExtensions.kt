package com.jonasgerdes.stoppelmap.util

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer

fun <T> LiveData<T>.observeWith(owner: LifecycleOwner, observer: (T) -> Unit) =
        observe(owner, Observer { data -> data?.let { observer(it) } })
