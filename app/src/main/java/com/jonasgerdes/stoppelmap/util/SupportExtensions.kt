package com.jonasgerdes.stoppelmap.util

import android.annotation.SuppressLint
import android.support.design.internal.BottomNavigationItemView
import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView
import android.util.Log


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
    for (i in 0..menuView.childCount - 1) {
        val item = menuView.getChildAt(i) as BottomNavigationItemView
        item.setShiftingMode(doHideText)
        //force update
        item.setChecked(item.itemData.isChecked)
    }
}