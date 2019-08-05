package com.jonasgerdes.androidutil.recyclerview

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
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

fun RecyclerView.doOnScrolledByUser(action: () -> Unit) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                action()
            }
        }
    })
}

fun RecyclerView.doOnScrolledFinished(action: () -> Unit) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                action()
            }
        }
    })
}

fun RecyclerView.findFirstCompletelyVisibleItemPosition() =
    findVisibleChild(0, layoutManager?.childCount ?: 0, true, false)?.let {
        getChildAdapterPosition(it)
    } ?: RecyclerView.NO_POSITION

fun RecyclerView.findVisibleChild(
    start: Int, end: Int, fullyVisible: Boolean,
    acceptPartiallyVisible: Boolean
): View? {

    val helper = if (layoutManager?.canScrollVertically() == true) {
        OrientationHelper.createVerticalHelper(layoutManager)
    } else {
        OrientationHelper.createHorizontalHelper(layoutManager)
    }

    val startPad = helper.startAfterPadding
    val endPad = helper.endAfterPadding
    val next = if (end > start) 1 else -1
    var partiallyVisible: View? = null
    var i = start
    try {
        while (i != end) {
            val child = layoutManager?.getChildAt(i)
            val childStart = helper.getDecoratedStart(child)
            val childEnd = helper.getDecoratedEnd(child)
            if (childStart < endPad && childEnd > startPad) {
                if (fullyVisible) {
                    if (childStart >= startPad && childEnd <= endPad) {
                        return child
                    } else if (acceptPartiallyVisible && partiallyVisible == null) {
                        partiallyVisible = child
                    }
                } else {
                    return child
                }
            }
            i += next
        }
    } catch (e: Exception) {
        Log.d("SupportExtensions", "can't find a visible child")
    }
    return partiallyVisible
}