package com.jonasgerdes.stoppelmap.util.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.LinearLayout
import com.jonasgerdes.stoppelmap.util.setTint
import org.jetbrains.anko.dip
import org.jetbrains.anko.imageView

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 21.07.17
 */
class IconList @JvmOverloads constructor(
        context: Context? = null,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    var iconTint = Color.BLACK
    var iconSize = 16
    var iconMargin = 2

    fun setIcons(icons: List<Int>) {
        removeAllViews()
        icons.forEach {
            val params = LinearLayout.LayoutParams(dip(iconSize), dip(iconSize))
            params.rightMargin = dip(iconMargin)
            params.leftMargin = dip(iconMargin)
            params.bottomMargin = dip(iconMargin)
            params.topMargin = dip(iconMargin)
            imageView(it) {
                setTint(iconTint)
            }.layoutParams = params
        }
        visibility = if (icons.isEmpty()) {
            android.view.View.GONE
        } else {
            android.view.View.VISIBLE
        }
    }
}