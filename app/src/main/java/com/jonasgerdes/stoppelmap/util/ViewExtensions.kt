package com.jonasgerdes.stoppelmap.util

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.support.annotation.LayoutRes
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager

fun ViewGroup.inflate(@LayoutRes layout: Int) =
        LayoutInflater.from(context).inflate(layout, this, false)

fun Activity.hideSoftkeyboard() = currentFocus?.let {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(it.windowToken, 0)
}

fun Activity.enableViewMarginFix(contentView: View, extraMargin: Int = 0) =
        KeyboardMarginHelper(this, contentView, extraMargin)

/**
 * Created mostly by mikepenz on 14.03.15.
 * This class implements a hack to change the layout padding on bottom if the keyboard is shown
 * to allow long lists with editTextViews
 * Basic idea for this solution found here: http://stackoverflow.com/a/9108219/325479
 */
class KeyboardMarginHelper(activity: Activity,
                           private val contentView: View,
                           private val extraMargin: Int = 0)
    : ViewTreeObserver.OnGlobalLayoutListener {

    private val decorView = activity.window.decorView
    private val visibleDisplayFrame = Rect()

    init {
        enable()
    }

    fun enable() = decorView.viewTreeObserver.addOnGlobalLayoutListener(this)
    fun disable() = decorView.viewTreeObserver.removeOnGlobalLayoutListener(this)

    override fun onGlobalLayout() {
        Log.d("ViewHelper", "onGlobalLayout()")
        //r will be populated with the coordinates of your view that area still visible.
        decorView.getWindowVisibleDisplayFrame(visibleDisplayFrame)

        //get screen height and calculate the difference with the useable area from the r
        val height = decorView.context.resources.displayMetrics.heightPixels
        val diff = height - visibleDisplayFrame.bottom - extraMargin

        //if it could be a keyboard add the padding to the view
        if (diff != 0) {
            // if the use-able screen height differs from the total screen height we assume that it shows a keyboard now
            //check if the padding is 0 (if yes set the padding for the keyboard)
            if (contentView.paddingBottom != diff) {
                //set the padding of the contentView for the keyboard
                contentView.setPadding(0, 0, 0, diff)
            }
        } else {
            //check if the padding is != 0 (if yes reset the padding)
            if (contentView.paddingBottom != 0) {
                //reset the padding of the contentView
                contentView.setPadding(0, 0, 0, 0)
            }
        }
    }
}