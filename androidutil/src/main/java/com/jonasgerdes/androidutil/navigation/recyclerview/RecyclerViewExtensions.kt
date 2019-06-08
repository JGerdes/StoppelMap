package com.jonasgerdes.androidutil.navigation.recyclerview

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


fun RecyclerView.onScrolledToEnd(itemThreshold: UInt = 1u, callback: (isOnEnd: Boolean) -> Unit) {
    var wasOnEnd = false
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val lastItem = when (val layoutManager = recyclerView.layoutManager) {
                is LinearLayoutManager -> layoutManager.findLastVisibleItemPosition()
                else -> 0
            }
            recyclerView.adapter?.itemCount?.let { itemCount ->
                val isOnEnd = itemCount - lastItem <= itemThreshold.toInt()
                if (isOnEnd != wasOnEnd) {
                    wasOnEnd = isOnEnd
                    callback(isOnEnd)
                }
            }
        }
    })
}

fun RecyclerView.removeAllItemDecorations() {
    while (itemDecorationCount > 0) {
        removeItemDecorationAt(0);
    }
}