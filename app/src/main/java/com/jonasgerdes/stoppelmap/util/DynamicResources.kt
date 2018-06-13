package com.jonasgerdes.stoppelmap.util

import android.content.Context
import android.content.res.ColorStateList
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.view.View
import android.widget.ImageView
import com.jonasgerdes.stoppelmap.model.map.entity.Image
import com.jonasgerdes.stoppelmap.model.map.entity.Stall


fun Context.getDrawableByName(resName: String, default: Int = 0) =
        resources.getIdentifier(resName, "drawable", packageName).let {
            if (it == 0) default else it
        }

fun Context.getColorByName(resName: String, default: Int = 0) =
        ContextCompat.getColor(this, resources.getIdentifier(resName, "color", packageName).let {
            if (it == 0) default else it
        })

fun CardView.setStallTypeBackgroundColor(type: String) {
    setCardBackgroundColor(context.getColorByName("background_stall_type_${type.fixNameForRes()}"))
}

fun ImageView.setStallColorTint(type: String) {
    imageTintList = ColorStateList.valueOf(context.getColorByName(
            "background_stall_type_${type.fixNameForRes()}"
    ))
}

fun Stall.getImagePath(image: Image?): String = image
        ?.let { "file:///android_asset/image/$type/${it.file}" }
        ?: "file:///android_asset/image/default/$type.png"

fun ImageView.setStallTypeDrawable(type: String) {
    setImageResource(context.getDrawableByName("ic_stall_type_${type.fixNameForRes()}"))
}

private fun String.fixNameForRes() = replace("-", "_")
