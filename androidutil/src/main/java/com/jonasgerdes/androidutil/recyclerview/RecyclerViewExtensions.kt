package com.jonasgerdes.androidutil.recyclerview

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

class OnCurrentItemChangedListener(private val callback: (itemId: Int) -> Unit) : RecyclerView.OnScrollListener() {
    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            val currentItem = when (val layoutManager = recyclerView.layoutManager) {
                is LinearLayoutManager -> layoutManager.findFirstCompletelyVisibleItemPosition()
                else -> -1
            }
            if (currentItem != -1) callback(currentItem)
        }
    }
}

fun RecyclerView.removeAllItemDecorations() {
    while (itemDecorationCount > 0) {
        removeItemDecorationAt(0);
    }
}