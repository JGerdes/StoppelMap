package com.jonasgerdes.stoppelmap.util

import android.content.Context
import android.widget.ImageView


fun Context.getDrawableByName(resName: String, default: Int = 0) =
        resources.getIdentifier(resName, "drawable", packageName).let {
            if (it == 0) default else it
        }

fun ImageView.setStallTypeDrawable(type: String) {
    val cleanedType = type.replace("-", "_")
    setImageResource(context.getDrawableByName("ic_stall_type_$cleanedType"))
}