package com.jonasgerdes.stoppelmap.map.view

import android.content.Context
import android.widget.ImageView
import com.jonasgerdes.stoppelmap.model.map.Type

fun Context.getDrawableByName(resName: String, default: Int = 0) =
    resources.getIdentifier(resName, "drawable", packageName).let {
        if (it == 0) default else it
    }

fun ImageView.setStallTypeDrawable(type: Type) {
    setImageResource(context.getDrawableByName("ic_stall_type_${type.nameForRes}"))
}

private val Type.nameForRes get() = type.replace("-", "_")