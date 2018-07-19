package com.jonasgerdes.stoppelmap.util

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import io.reactivex.subjects.BehaviorSubject

fun ViewGroup.inflate(@LayoutRes layout: Int) =
        LayoutInflater.from(context).inflate(layout, this, false)

fun Activity.hideSoftkeyboard() = currentFocus?.let {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(it.windowToken, 0)
}

fun TextView.setTextOrHide(text: String?) {
    visibility = if (text == null) View.GONE else View.VISIBLE
    this.text = text
}

fun TextView.setTextOrDefault(text: String?, @StringRes defaultRes: Int) {
    if (text != null) {
        setText(text)
    } else {
        setText(defaultRes)
    }
}

/**
 * Inspired by mikepenz (https://raw.githubusercontent.com/mikepenz/MaterialDrawer/fed00a199196efbc63296a081723e603647b5bc4/library/src/main/java/com/mikepenz/materialdrawer/util/KeyboardUtil.java)
 * Basic idea for this solution found here: http://stackoverflow.com/a/9108219/325479
 */
class KeyboardDetector(activity: Activity)
    : ViewTreeObserver.OnGlobalLayoutListener {

    private val decorView = activity.window.decorView
    private val visibleDisplayFrame = Rect()
    private val keyboardShowingSubject = BehaviorSubject.create<Boolean>()
    val keyboardChanges = keyboardShowingSubject.hide()

    init {
        enable()
    }

    fun enable() = decorView.viewTreeObserver.addOnGlobalLayoutListener(this)
    fun disable() = decorView.viewTreeObserver.removeOnGlobalLayoutListener(this)

    override fun onGlobalLayout() {
        //r will be populated with the coordinates of your view that area still visible.
        decorView.getWindowVisibleDisplayFrame(visibleDisplayFrame)

        //get screen height and calculate the difference with the useable area from the r
        val height = decorView.context.resources.displayMetrics.heightPixels
        val diff = height - visibleDisplayFrame.bottom

        keyboardShowingSubject.onNext(diff != 0)
    }
}