package com.jonasgerdes.stoppelmap.core.util

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

inline fun <T> LifecycleOwner.observe(liveData: LiveData<T>, crossinline observer: (T) -> Unit) =
    liveData.observe(this.getViewLifecycleOwnerIfFragment(), Observer { observer(it!!) })


fun LifecycleOwner.getViewLifecycleOwnerIfFragment() =
    if (this is Fragment) this.viewLifecycleOwner else this