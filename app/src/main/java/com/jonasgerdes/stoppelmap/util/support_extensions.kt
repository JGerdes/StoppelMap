package com.jonasgerdes.stoppelmap.util

import android.annotation.SuppressLint
import android.support.design.internal.BottomNavigationItemView
import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.jakewharton.rxbinding2.support.v7.widget.scrollEvents
import java.util.concurrent.TimeUnit


/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 11.06.2017
 */

fun BottomNavigationView.enableItemShifting(doShift: Boolean) {
    val menuView = getChildAt(0) as BottomNavigationMenuView
    try {
        val shiftingMode = menuView.javaClass.getDeclaredField("mShiftingMode")
        shiftingMode.isAccessible = true
        shiftingMode.setBoolean(menuView, doShift)
        shiftingMode.isAccessible = false
    } catch (e: IllegalAccessException) {
        Log.e("SupportExtension", "Can't get field mShiftingMode", e)
    } catch (e: NoSuchFieldException) {
        Log.e("SupportExtension", "Can't change field mShiftingMode", e)
    }

}


@SuppressLint("RestrictedApi")
fun BottomNavigationView.enableItemTextHiding(doHideText: Boolean) {
    val menuView = getChildAt(0) as BottomNavigationMenuView
    for (i in 0 until menuView.childCount) {
        val item = menuView.getChildAt(i) as BottomNavigationItemView
        item.setShiftingMode(doHideText)
        //force update
        item.setChecked(item.itemData.isChecked)
    }
}

fun RecyclerView.findVisibleChild(start: Int, end: Int, fullyVisible: Boolean,
                                  acceptPartiallyVisible: Boolean): View? {

    val helper = if (layoutManager.canScrollVertically()) {
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
            val child = layoutManager.getChildAt(i)
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

fun RecyclerView.findFirstCompletelyVisibleItemPosition() =
        findVisibleChild(0, layoutManager.childCount, true, false)?.let {
            getChildAdapterPosition(it)
        } ?: RecyclerView.NO_POSITION


fun RecyclerView.itemScrolls() = scrollEvents()
        .sample(100, TimeUnit.MILLISECONDS)
        .map { findFirstCompletelyVisibleItemPosition() }
        .filter { it != RecyclerView.NO_POSITION }
        .distinctUntilChanged()