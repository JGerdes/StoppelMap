package com.jonasgerdes.stoppelmap.map.view

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.jonasgerdes.stoppelmap.model.map.Type

fun Context.getDrawableByName(resName: String, default: Int = 0) =
    resources.getIdentifier(resName, "drawable", packageName).let {
        if (it == 0) default else it
    }

fun ImageView.setStallTypeDrawable(type: Type) {
    setImageResource(context.getDrawableByName("ic_stall_type_${type.nameForRes}"))
}

fun ImageView.setDrawableFromTypeOrHide(name: String) {
    setDrawableOrHide("ic_stall_type_${name.forRes}")
}

fun ImageView.setDrawableFromItemOrHide(name: String) {
    setDrawableOrHide("ic_stall_item_${name.forRes}")
}

fun ImageView.setDrawableOrHide(name: String) {
    val drawable = context.getDrawableByName(name)
    if (drawable == 0) {
        visibility = View.INVISIBLE
    } else {
        visibility = View.VISIBLE
        setImageResource(drawable)
    }
}

private val Type.nameForRes get() = type.forRes
private val String.forRes get() = replace("-", "_")